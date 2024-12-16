package com.demo.gidermanagement

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.datasource.TransactionDatabase
import com.demo.gidermanagement.navigation.NavGraph
import com.demo.gidermanagement.ui.settings.preferences.PreferencesViewModel
import com.demo.gidermanagement.ui.settings.preferences.data.LanguageItem
import com.demo.gidermanagement.ui.theme.GiderYönetimiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var transactionRepository: TransactionRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(Constants.SharedPreferences.shared_pref_name, Context.MODE_PRIVATE)
        val language = sharedPreferences.getString(Constants.SharedPreferences.appLanguage, "en") ?: "en"

        if(sharedPreferences.getString(Constants.SharedPreferences.appLanguage,null) == null){
            savePreferences(sharedPreferences)
        } else if(sharedPreferences.getBoolean(Constants.SharedPreferences.updateCategories,false)){
            val categoriesArray = resources.getStringArray(R.array.init_category_list)
            lifecycleScope.launch {
                updateFirstAndSecondCategory(transactionRepository,categoriesArray)
            }
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setAppLocale(applicationContext,language)


        //preferencesViewModel = ViewModelProvider(this)[PreferencesViewModel::class.java]
        //LocaleHelper().setLocale(this@MainActivity, language)*/

        setContent {
            GiderYönetimiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  NavGraph()
                }
            }
        }

    }

    override fun attachBaseContext(newBase: Context) {
        val locale = AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()
        val config = newBase.resources.configuration.apply { setLocale(locale) }
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onResume() {
        super.onResume()
    }
}
private suspend fun updateFirstAndSecondCategory(
    transactionRepository: TransactionRepository,
    categoriesArray: Array<String>
) {
        transactionRepository.getAllCategory().collectLatest { newCategories ->
            if (newCategories.isNotEmpty()) {
                // İlk öğeleri güncelle
                newCategories[Constants.NumberConstants.ZERO].transactionCategory =
                    categoriesArray.getOrNull(Constants.NumberConstants.ZERO) ?: ""
                newCategories[Constants.NumberConstants.ONE].transactionCategory =
                    categoriesArray.getOrNull(Constants.NumberConstants.ONE) ?: ""
                newCategories[Constants.NumberConstants.TWO].transactionCategory =
                    categoriesArray.getOrNull(Constants.NumberConstants.TWO) ?: ""
                newCategories[Constants.NumberConstants.THREE].transactionCategory =
                    categoriesArray.getOrNull(Constants.NumberConstants.THREE) ?: ""
                newCategories[Constants.NumberConstants.FOUR].transactionCategory =
                    categoriesArray.getOrNull(Constants.NumberConstants.FOUR) ?: ""
                transactionRepository.updateCategory(newCategories)
            }
        }

}



fun setAppLocale(context: Context, languageTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Android 13 ve üzeri
        val localeList = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(localeList)
    } else {
        // Android 12 ve altı
        val localeList = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(localeList)

        // Eski cihazlar için kaynakları manuel olarak güncelle
        updateResourcesForLegacy(context, languageTag)
    }
}
fun getStringArray(context: Context, arrayResId: Int): Array<String> {
    val locale = AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()
    val config = context.resources.configuration.apply { setLocale(locale) }
    val updatedResources = context.createConfigurationContext(config).resources
    return updatedResources.getStringArray(arrayResId)
}

fun updateResourcesForLegacy(context: Context, languageTag: String) {
    val locale = Locale.forLanguageTag(languageTag)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

private fun savePreferences(sharedPreferences: SharedPreferences){
    val locale = Locale.getDefault()
    val country = locale.country // Örneğin: "US"
    val europeCountries = setOf(
        "AT", "AL", "AM", "AD", "BY", "BE", "BA", "BG", "HR", "CY",
        "CZ", "DK", "EE", "FI", "FR", "GE", "DE", "GR", "HU", "IS",
        "IE", "IT", "KZ", "XK", "LV", "LI", "LT", "LU", "MK", "MD",
        "ME", "NL", "NO", "PL", "PT", "RO", "RU", "RS", "SK", "SI",
        "ES", "SE", "CH", "UA", "VA"
    )

    val defaultCurrency: String
    val defaultLanguage: String

    if (country == "TR") {
        defaultCurrency = "₺"
        defaultLanguage = "tr"
    } else if (country == "US") {
        defaultCurrency = "$"
        defaultLanguage = "en"
    } else if (country == "GB") {
        defaultCurrency = "£"
        defaultLanguage = "en"
    } else if (country == "DE") {
        defaultCurrency = "£"
        defaultLanguage = "de"
    } else if (country == "ES") {
        defaultCurrency = "£"
        defaultLanguage = "es"
    }
    else if(europeCountries.contains(country)) {
        defaultCurrency = "€"
        defaultLanguage = "en"
    } else {
        defaultCurrency =  "$"
        defaultLanguage = "en"
    }
    sharedPreferences.edit().putString(Constants.SharedPreferences.appLanguage, defaultLanguage).apply()
    sharedPreferences.edit().putString(Constants.SharedPreferences.selectedCurrency, defaultCurrency).apply()
    sharedPreferences.edit().putBoolean(Constants.SharedPreferences.isDarkMode, false).apply()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GiderYönetimiTheme {
    }
}
