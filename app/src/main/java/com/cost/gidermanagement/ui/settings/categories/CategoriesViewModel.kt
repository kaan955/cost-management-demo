package com.demo.gidermanagement.ui.settings.categories

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.common.model.CategoryItem
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.datasource.TransactionDao
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.ui.home.model.TransactionInfoItem
import com.demo.gidermanagement.ui.settings.model.SettingsItem
import com.demo.gidermanagement.ui.settings.preferences.PreferencesuiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class CategoriesuiState(
    var test: Boolean = false,
    var categoryList: List<CategoryItem> = emptyList(),
)

@HiltViewModel
class CategoriesViewModel@Inject constructor(
    private val transactionRepository: TransactionRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesuiState())
    val uiState: StateFlow<CategoriesuiState> = _uiState.asStateFlow()

    init {
        getAllCategory()
    }

    private fun getAllCategory() {

        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.getAllCategory().collectLatest {
                val allCategory: ArrayList<CategoryItem> = ArrayList()

                if (it.isNotEmpty()) {
                    for (item in it) {
                        allCategory.add(
                            CategoryItem(
                                item.id,
                                item.transactionCategory
                            )
                        )
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            categoryList = allCategory
                        )
                    }
                }
            }
        }
    }



    fun saveCategory(categoryName: String) {
        val category = TransactionCategoriesEntity(
            transactionCategory = categoryName
        )

        viewModelScope.launch {
            try {
                transactionRepository.insertCategory(category)
            getAllCategory()
            } catch (dataError: SQLiteException) {
                Timber.tag("NotificationViewModel").e(dataError, "Error saving category")
            }
        }
    }

    fun deleteCategory(category: CategoryItem) {
        viewModelScope.launch {
            transactionRepository.deleteCategory(
                TransactionCategoriesEntity(category.categoryID?: -1,category.categoryName)
            )
        }.also {
            getAllCategory()
        }
    }
}
