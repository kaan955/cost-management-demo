package com.demo.gidermanagement.ui.home

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.ui.home.model.FilterData
import com.demo.gidermanagement.ui.home.model.TransactionInfoItem
import com.demo.gidermanagement.common.utils.ResourcesProvider
import com.demo.gidermanagement.common.utils.logEvent
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.ui.home.component.TransactionGraphItem
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.absoluteValue


data class HomeUiState(
    var transactionList: ArrayList<TransactionInfoItem> = ArrayList(),
    var filteredTransactionList: ArrayList<TransactionInfoItem> = ArrayList(),
    var futureTransactionList: ArrayList<TransactionInfoItem> = ArrayList(),
    var selectedHomePageFilter: Int = 3,
    var filterArray: ArrayList<FilterData> = ArrayList(),
    var transactionGraphList: ArrayList<TransactionGraphItem> = ArrayList(),
    var incomeTotal: Double = 0.0,
    var expensesTotal: Double = 0.0,
    var incomeTotalStr: String = "0.0",
    var expensesTotalStr: String = "0.0",
    var loading: Boolean = true
)

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val transactionRepository: TransactionRepository,
    private val resourcesProvider: ResourcesProvider,
    firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedCurrency = mutableStateOf(
        sharedPreferences.getString(
            Constants.SharedPreferences.selectedCurrency,"$"
        ) ?: "$")
    val selectedCurrency: State<String> = _selectedCurrency

    init {
        initHomePageFilter()
        initCategory()
        firebaseAnalytics.logEvent(Constants.AnalyticsParameter.HOME_VISIT,viewModelScope)
    }
    private fun getTransactions() {
        viewModelScope.launch(IO) {
            transactionRepository.getAllTransaction().collectLatest {
                val allTransactions: ArrayList<TransactionInfoItem> = ArrayList()

                for (item in it){
                    allTransactions.add(TransactionInfoItem(
                        item.id,
                        item.transactionDate,
                        item.transactionDesc,
                        item.transactionCategory,
                        item.transactionAmount.toString(),
                        null,
                        item.isExpenses,
                    ))
                }

                // Tarihe göre sıralama
                val sortedTransactions = ArrayList(allTransactions.sortedBy { transaction ->
                    // String tarih formatı ile sıralama
                    transaction.transactionDate
                })

                _uiState.update { currentState ->
                    currentState.copy(
                        transactionList = sortedTransactions
                    )
                }.also {
                    updateFilteredTransactions(uiState.value.selectedHomePageFilter)
                }
            }
        }
    }

    private fun updateFilteredTransactions(updatedSelectedFilter: Int) {
        val selectedFilter = updatedSelectedFilter
        var filteredTransactions: ArrayList<TransactionInfoItem> = ArrayList()
        var futureTransactionList: ArrayList<TransactionInfoItem> = ArrayList()
        val currentDate = LocalDate.now()

        for(item in uiState.value.transactionList) {
            if (item.transactionDate != null) {
                val df: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                val localDate = LocalDate.parse(item.transactionDate, df)
                val numberOfDays =
                    Duration.between(localDate.atStartOfDay(), currentDate.atStartOfDay()).toDays()

                if (uiState.value.filterArray[selectedFilter].dayCount >= numberOfDays && numberOfDays >= 0) {
                    filteredTransactions.add(item)
                } else if(numberOfDays < 0) {
                    item.remainingDays = numberOfDays.toInt().absoluteValue
                    futureTransactionList.add(item)
                }
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                filteredTransactionList = filteredTransactions,
                futureTransactionList = futureTransactionList
            ).also {
                setGraphData(filteredTransactions)
            }
        }
    }

    private fun setGraphData(filteredTransactions: ArrayList<TransactionInfoItem>){
        var list = filteredTransactions

        val expensesList = list
            .filter { it.isExpenses }

        val expensesTotal = expensesList
            .sumOf { it.transactionAmount.toDouble() }

        val incomeList = list
            .filter { !it.isExpenses }

        val incomeTotal = incomeList
            .sumOf { it.transactionAmount.toDouble() }

        val total = expensesTotal + incomeTotal

        val expensesPercentage =
            (expensesTotal / total) * Constants.HomeScreen.percentageMultiplier
        val incomePercentage =
            (incomeTotal / total) * Constants.HomeScreen.percentageMultiplier

        val transactionGraphList: ArrayList<TransactionGraphItem> = ArrayList()
        transactionGraphList.add(TransactionGraphItem(resourcesProvider.getString(R.string.income_type),
            if (incomePercentage.isNaN()) Constants.HomeScreen.defaultGraphPercentage
            else incomePercentage)
        )
        transactionGraphList.add(TransactionGraphItem(
            resourcesProvider.getString(R.string.outgoings_type
            ),
            if (expensesPercentage.isNaN()) Constants.HomeScreen.defaultGraphPercentage
            else expensesPercentage)
        )

        _uiState.update { currentState ->
            currentState.copy(
                transactionGraphList = transactionGraphList,
                incomeTotal = incomeTotal,
                expensesTotal = expensesTotal,
                incomeTotalStr = incomeTotal.toString() + selectedCurrency.value,
                expensesTotalStr = expensesTotal.toString() + selectedCurrency.value,
                loading = false
            )
        }
    }

    private fun initCategory() {
        viewModelScope.launch(IO) {
            val categories = transactionRepository.getAllCategory().firstOrNull()
            if (categories.isNullOrEmpty()) {
                val categoriesArray = resourcesProvider.getStringArray(R.array.init_category_list)
                val categoryEntities = categoriesArray.map { category ->
                    TransactionCategoriesEntity(transactionCategory = category)
                }
                transactionRepository.insertCategory(categoryEntities)
            }
        }
    }


    private fun initHomePageFilter() {
        var filterArray: ArrayList<FilterData> = ArrayList()

        filterArray.add(
            FilterData(Constants.NumberOfDayCountConstants.TODAY,
                resourcesProvider.getString(R.string.homepage_filter_0)
            )
        )
        filterArray.add(
            FilterData(
                Constants.NumberOfDayCountConstants.LAST_THREE_DAYS,
                resourcesProvider.getString(R.string.homepage_filter_1
                )
            )
        )
        filterArray.add(
            FilterData(
                Constants.NumberOfDayCountConstants.LAST_SEVEN_DAYS,
                resourcesProvider.getString(R.string.homepage_filter_2)
            )
        )
        filterArray.add(
            FilterData(Constants.NumberOfDayCountConstants.LAST_ONE_MONTH,
                resourcesProvider.getString(R.string.homepage_filter_3)
            )
        )
        filterArray.add(
            FilterData(Constants.NumberOfDayCountConstants.LAST_THREE_MONTHS,
                resourcesProvider.getString(R.string.homepage_filter_4)
            )
        )
        filterArray.add(
            FilterData(Constants.NumberOfDayCountConstants.LAST_ONE_YEAR,
                resourcesProvider.getString(R.string.homepage_filter_5)
            )
        )

        _uiState.update { currentState ->
            currentState.copy(
                filterArray = filterArray
            )
        }.also {
            getTransactions()
        }
    }
    fun updateHomePageFilter(status: Int) {
        if (!(status == -1 && uiState.value.selectedHomePageFilter == Constants.NumberConstants.ZERO) &&
            !(status == 1 && uiState.value.selectedHomePageFilter == Constants.NumberConstants.FIVE)) {
            var newStatus = uiState.value.selectedHomePageFilter
                newStatus += status
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedHomePageFilter = newStatus
                    ).also {
                        updateFilteredTransactions(newStatus)
                    }
                }
            }
    }
}
