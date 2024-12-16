package com.demo.gidermanagement.ui.detailList

import android.content.SharedPreferences
import android.os.Bundle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.common.model.CategoryItem
import com.demo.gidermanagement.common.utils.logEvent
import com.demo.gidermanagement.common.utils.logTransactionEvent
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.entity.TransactionEntity
import com.demo.gidermanagement.ui.detailList.component.CategoryGraphItem
import com.demo.gidermanagement.ui.detailList.model.CategoryFilter
import com.demo.gidermanagement.ui.home.model.TransactionInfoItem
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



data class DetailListUiState(
    var desc:String? = null,
    var transactionList: ArrayList<TransactionInfoItem> = ArrayList(),
    var showFilterSheet: Boolean = false,
    var selectedRadioPaymentType: Int = 2,
    var filterCategoryList: ArrayList<CategoryFilter> = ArrayList(),
    var incomePercentage: Float = 0.0f,
    var expensesPercantage: Float = 0.0f,
    var categoryGraphList: ArrayList<CategoryGraphItem> = ArrayList(),
    var transactionListSize: Int = 0,
    var expensesListSize: Int = 0,
    var incomeListSize: Int = 0,
    var maxIncomeValue: Double = 0.0,
    var maxExpensesValue: Double = 0.0
)

@HiltViewModel
class DetailListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sharedPreferences: SharedPreferences,
    private val firebaseAnalytics: FirebaseAnalytics
    ): ViewModel() {

    private val _uiState = MutableStateFlow(DetailListUiState())
    val uiState: StateFlow<DetailListUiState> = _uiState.asStateFlow()

    private val _selectedCurrency = mutableStateOf(
        sharedPreferences.getString(Constants.SharedPreferences.selectedCurrency,"$")
            ?: "$")
    val selectedCurrency: State<String> = _selectedCurrency
    init {
        getTransactions()
        getAllFilterCategory()
        firebaseAnalytics.logEvent(Constants.AnalyticsParameter.DETAIL_LIST_VISIT,viewModelScope)
    }




    private fun getTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.getAllTransaction().collectLatest {
                var allTransactions: ArrayList<TransactionInfoItem> = ArrayList()

                for (item in it){
                    allTransactions.add(
                        TransactionInfoItem(
                        item.id,
                        item.transactionDate,
                        item.transactionDesc,
                        item.transactionCategory,
                        item.transactionAmount.toString(),
                            0,
                            item.isExpenses
                    )
                    )
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        transactionList = allTransactions
                    )
                } .also {
                    if(allTransactions.isNotEmpty()) {
                        filteredLists()
                    }
                }
            }
        }
    }

    private fun filteredLists() {
        val list = uiState.value.transactionList

        val expensesList = list
            .filter { it.isExpenses }

        val expensesTotal = expensesList
            .sumOf { it.transactionAmount.toDouble() }

        val incomeList = list
            .filter { !it.isExpenses }

        val incomeTotal = incomeList
            .sumOf { it.transactionAmount.toDouble() }

        val categoryTotals = list.groupBy { it.transactionCategory }
            .mapValues { entry -> entry.value.sumOf { it.transactionAmount.toDouble() } }

        val categoryTotalsList = categoryTotals.entries
            .sortedByDescending { it.value }
            .map { (category, totalAmount) -> CategoryGraphItem(category, totalAmount) }

        var sortedCategoryList: ArrayList<CategoryGraphItem> = ArrayList()
        var totalPercentage = 0.0

        if (categoryTotalsList.isNotEmpty()) {
            for (item in categoryTotalsList.take(Constants.NumberConstants.FIVE)) {
                val percentage =
                    ((item.categoryPercentage ?: 0.0) / (expensesTotal + incomeTotal)) *
                            Constants.HomeScreen.percentageMultiplier
                if(percentage > 0.0) {
                    sortedCategoryList.add(CategoryGraphItem(item.categoryName, percentage))
                }
                totalPercentage += percentage
            }
            if(Constants.HomeScreen.percentageMultiplier - totalPercentage > 0.0) {
                sortedCategoryList.add(CategoryGraphItem(
                    "Other",
                    Constants.HomeScreen.percentageMultiplier - totalPercentage)
                )
            }
        }

        val total = expensesTotal + incomeTotal
        val incomePercentage = (incomeTotal / total) * Constants.HomeScreen.percentageMultiplier
        val expensesPercantage = (expensesTotal / total) * Constants.HomeScreen.percentageMultiplier

        _uiState.update { currentState ->
            currentState.copy(
                expensesPercantage = expensesPercantage.toFloat(),
                incomePercentage = incomePercentage.toFloat(),
                categoryGraphList = sortedCategoryList,
                incomeListSize = incomeList.size,
                expensesListSize = expensesList.size,
                transactionListSize = list.size,
                maxIncomeValue = incomeList
                    .maxOfOrNull { it.transactionAmount.toDoubleOrNull() ?: 0.0 } ?: 0.0,
                maxExpensesValue = expensesList
                    .maxOfOrNull { it.transactionAmount.toDoubleOrNull() ?: 0.0 } ?: 0.0
            )
        }
    }

    fun updateFilterSheetStatus(newStatus: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
             showFilterSheet = newStatus
            )
        }
    }

    fun setPaymentType(selected: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedRadioPaymentType = selected
            )
        }
    }

    fun categorySelection(categoryItem: CategoryFilter) {
        var newList = uiState.value.filterCategoryList

        newList = newList.map {
            if (it.categoryName == categoryItem.categoryName) categoryItem else it
        } as ArrayList<CategoryFilter>


        _uiState.update { currentState ->
            currentState.copy(
                filterCategoryList = newList
            )
        }
    }

    fun filteredTransaction() {

        var paymentType = uiState.value.selectedRadioPaymentType
        var paymentTypeList: ArrayList<Boolean> = ArrayList()

        var categoryList = uiState.value.filterCategoryList

        var selectedCategoryList = categoryList.filter {it.checked}



        if (paymentType == 0) {
            paymentTypeList.add(true)
        } else if(paymentType == 1) {
            paymentTypeList.add(false)
        } else if(paymentType == 2) {
            paymentTypeList.add(false)
            paymentTypeList.add(true)
        }
        var filteredTransaction = uiState.value.transactionList


        filteredTransaction = filteredTransaction.filter {
            paymentTypeList.contains(it.isExpenses)
        } as ArrayList<TransactionInfoItem>

        if (selectedCategoryList.isNotEmpty()) {
            filteredTransaction.filter { transaction ->
                selectedCategoryList.any { category -> transaction.transactionCategory == category.categoryName }
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                showFilterSheet = false,
                transactionList = filteredTransaction
            )
        }
    }

    private fun getAllFilterCategory() {

        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.getAllCategory().collectLatest {
                val allCategory: ArrayList<CategoryFilter> = ArrayList()

                if (it.isNotEmpty()) {
                    for (item in it) {
                        allCategory.add(
                            CategoryFilter(
                                item.transactionCategory?: "",
                          false
                            )
                        )
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            filterCategoryList = allCategory
                        )
                    }
                }
            }
        }
    }

    fun deleteTransactionData(transactionEntity : TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.delete(transactionEntity)
        }.also {
            val transactionItem  = TransactionInfoItem(
                transactionEntity.id,
                transactionEntity.transactionDate,
                transactionEntity.transactionDesc,
                transactionEntity.transactionCategory,
                transactionEntity.transactionAmount.toString()
            )
            var deletedTransactionList = uiState.value.transactionList
            deletedTransactionList.remove(transactionItem)
            _uiState.update { currentState ->
                currentState.copy(
                    transactionList = deletedTransactionList
                )
            }
            firebaseAnalytics.logEvent(Constants.AnalyticsParameter.TRANSACTION_DELETED,viewModelScope)
        }
    }
}
