package com.demo.gidermanagement.ui.settings.preferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.MainActivity
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.common.utils.ResourcesProvider
import com.demo.gidermanagement.common.utils.findActivity
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.ui.detailList.model.CategoryFilter
import com.demo.gidermanagement.ui.settings.preferences.data.LanguageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

data class PreferencesuiState(
    var isDarkMode: Boolean = false,
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val transactionRepository: TransactionRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesuiState())
    val uiState: StateFlow<PreferencesuiState> = _uiState.asStateFlow()

    private var _navigateToHomeScreen = MutableStateFlow(false)
    var navigateToHomeScreen : StateFlow<Boolean> = _navigateToHomeScreen.asStateFlow()


    private var languageCode = sharedPreferences.getString(Constants.SharedPreferences.appLanguage, "en") ?: "en"
    private val languages = Constants.PreferencesScreen.languageItems

    val index = languages.indexOfFirst { it.languageCode == languageCode }


    private val _appLanguage = mutableStateOf(if(index != -1) languages[index].languageTitle?: "English" else "English")
    val appLanguage: State<String> = _appLanguage


    private val _languageChanged = MutableStateFlow<String>(appLanguage.value)
    // Varsayılan dil 'en'
    val languageChanged: StateFlow<String> get() = _languageChanged
    private val _selectedCurrency = mutableStateOf(
        sharedPreferences.getString(
            Constants.SharedPreferences.selectedCurrency,"$"
        ) ?: "$")
    val selectedCurrency: State<String> = _selectedCurrency

    private val _isDarkMode = mutableStateOf(
        sharedPreferences.getBoolean(Constants.SharedPreferences.isDarkMode,false
        )
    )
    val isDarkMode: State<Boolean> = _isDarkMode


    fun saveLanguage(languageItem: LanguageItem) {
        _appLanguage.value = languageItem.languageTitle ?: "English"
        viewModelScope.launch {
            sharedPreferences.edit()
                .putString(Constants.SharedPreferences.appLanguage, languageItem.languageCode)
                .apply()
            setApplicationLanguage(languageItem.languageCode ?: "en")
        }
    }
    private fun setApplicationLanguage(languageTag: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean(Constants.SharedPreferences.updateCategories,true).apply()
        }

        val localeList = LocaleListCompat.forLanguageTags(languageTag) // Örn. "tr" Türkçe için
        AppCompatDelegate.setApplicationLocales(localeList)
    }
    fun saveTheme(isDark: Boolean) {
        _isDarkMode.value = isDark
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean(Constants.SharedPreferences.isDarkMode, isDark).apply()
        }
    }

    fun saveCurrency(currency: String) {
        _selectedCurrency.value = currency
        viewModelScope.launch {
            sharedPreferences.edit().putString(Constants.SharedPreferences.selectedCurrency, currency).apply()
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.clearTransactionTable()
            transactionRepository.clearTransactionCategoriesTable()
            sharedPreferences.edit().clear().apply()
            _navigateToHomeScreen.value = true
        }
    }
}
