package com.demo.gidermanagement.ui.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.common.model.CategoryItem
import com.demo.gidermanagement.common.utils.ResourcesProvider
import com.demo.gidermanagement.common.utils.logEvent
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.data.entity.TransactionEntity
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


data class AddTransactionUiState(
    var desc:String? = null,
    var date: String? = null,
    var category: String? = null,
    var amount: String = "0",
    var isExpenses: Boolean = false,
    var itemID: Int = -1,
    var transactionType: Int = 0,
    var deletePopupShow: Boolean = false,
    var transactionEntity: TransactionEntity? = null,
    var categoryList: ArrayList<CategoryItem> = ArrayList(),
    var selectedCategoryID: Int = 0,
    var showCategoryBottomSheet: Boolean = false,
    var navigationText: String = "",
    var openAlertDialog: Boolean = false
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle,
    private val resourcesProvider: ResourcesProvider,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {
    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()
    private val itemID : Int? = savedStateHandle[Constants.NavigationParameter.TRANSACTION_ID]

    private var _navigateToHomeScreen = MutableStateFlow(false)
    var navigateToHomeScreen : StateFlow<Boolean> = _navigateToHomeScreen.asStateFlow()


    init {
        initDate()
        setCategoryList()
        setTransactionDetails(itemID)
        firebaseAnalytics.logEvent(Constants.AnalyticsParameter.ADD_TRANSACTION_VISIT,viewModelScope)
    }

        fun updateDate(newDate: String) {
            _uiState.update { currentState->
                currentState.copy(
                    date = newDate
                )
            }
        }

        fun updateDesc(newDesc: String) {
            _uiState.update { currentState->
                currentState.copy(
                    desc = newDesc
                )
            }
        }

        fun updateCategory() {
            val categoryStr = uiState.value.categoryList[uiState.value.selectedCategoryID]
            _uiState.update { currentState->
                currentState.copy(
                    category = categoryStr.categoryName,
                    showCategoryBottomSheet = false
                )
            }
        }


        fun updateAmount(newCharacter: String) {

            var newAmountStr = uiState.value.amount

            if(newAmountStr == "0") {
                newAmountStr = ""
            }

            if(newCharacter == ",") {
                if (!uiState.value.amount.contains(',')) {
                    newAmountStr += newCharacter
                }
            } else if(newCharacter == "DEL") {
                if(newAmountStr.isNotEmpty()) {
                    newAmountStr = newAmountStr.substring(0, newAmountStr.length - 1)
                }
            }else{
                newAmountStr += newCharacter
            }

            if(newAmountStr.contains(",")) {
                newAmountStr = newAmountStr.replace(',','.')
            }

            _uiState.update { currentState->
                currentState.copy(
                 amount = newAmountStr
                )
            }
        }

    fun updateTransactionType(isExpenses: Boolean) {
        _uiState.update { currentState->
            currentState.copy(
                isExpenses = isExpenses
            )
        }
    }

        private fun initDate() {
            val date = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
            )

            _uiState.update { currentState->
                currentState.copy(
                    date = date
                )
            }


        }

        private fun setTransactionDetails(transactionID : Int?) {
                _uiState.update { currentState->
                    currentState.copy(
                        transactionType =
                        if (transactionID == null ||
                            transactionID == Constants.TransactionItemStatus.newTransactionItem
                            )
                            Constants.TransactionStatus.newTransaction
                        else Constants.TransactionStatus.updateTransaction
                    )
                }.also {
                    if (uiState.value.transactionType == Constants.TransactionStatus.updateTransaction) {
                    setUpdateTransactionData()
                    }
                }


        }
    private fun setUpdateTransactionData() {

        viewModelScope.launch(IO) {
            val item = transactionRepository.getSingleTransaction(itemID!!)
            val categories = transactionRepository.getAllCategory().first()
            //init category
            val list = categories
            var categoryIndex = 0

            list.forEachIndexed { index, value ->
                if (value.transactionCategory == item.transactionCategory) {
                    categoryIndex = index
                }
            }
            _uiState.update { currentState->
                currentState.copy(
                    date = item.transactionDate,
                    desc = item.transactionDesc,
                    category = item.transactionCategory,
                    selectedCategoryID = categoryIndex,
                    amount = item.transactionAmount.toString(),
                    isExpenses = item.isExpenses,
                    transactionEntity = item
                )
            }
        }
    }

        fun deleteTransactionData(transactionEntity : TransactionEntity) {
            _uiState.update { currentState->
                currentState.copy(
                    deletePopupShow = false
                )
            }
            viewModelScope.launch(IO) {
                transactionRepository.delete(transactionEntity)
            }.also {
                _uiState.update { currentState->
                    currentState.copy(
                        navigationText = resourcesProvider.getString(R.string.record_delete)
                    )
                }.also {
                    firebaseAnalytics.logEvent(Constants.AnalyticsParameter.TRANSACTION_DELETED,viewModelScope)
                    _navigateToHomeScreen.value = true
                }
            }
        }

        fun deleteTransactionPopupStatus(status: Boolean) {
            _uiState.update { currentState->
                currentState.copy(
                    deletePopupShow = status
                )
            }
        }

    fun updateFilterSheetStatus(newStatus: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showCategoryBottomSheet = newStatus
            )
        }
    }

        private fun setCategoryList() {

            viewModelScope.launch(IO) {
                val categories = transactionRepository.getAllCategory().first()

                    var allCategory: ArrayList<CategoryItem> = ArrayList()

                    if(categories.isNotEmpty()) {
                        for (item in categories){
                            allCategory.add(
                                CategoryItem(
                                    item.id,
                                    item.transactionCategory
                                )
                            )
                        }
                        //init category
                        val list = allCategory
                        var categoryName = list[Constants.AddTransactionScreen.defaultCategoryIndex].categoryName
                        var categoryIndex = Constants.AddTransactionScreen.defaultCategoryIndex

                        list.forEachIndexed { index, value ->
                            if (value.categoryName == resourcesProvider.getString(R.string.category_other)) {
                                categoryIndex = index
                                categoryName = resourcesProvider.getString(R.string.category_other)
                            }
                        }

                        _uiState.update { currentState ->
                            currentState.copy(
                                categoryList = allCategory,
                                category = categoryName,
                                selectedCategoryID = categoryIndex
                            )
                        }
                    }

            }
        }

    fun addCategory(item: CategoryItem) {
        viewModelScope.launch(IO) {
            transactionRepository.insertCategory(TransactionCategoriesEntity(0, item.categoryName))
        }
    }

    fun setCategoryID(selected: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategoryID = selected
            )
        }
    }

        fun verifySave() {
            if( uiState.value.amount == "" || uiState.value.amount == "0" ) {
                _uiState.update { currentState ->
                    currentState.copy(
                        openAlertDialog = true
                    )
                }
            } else {
                saveTransactionData()
            }

        }

        fun closeWarningDialog() {
            _uiState.update { currentState ->
                currentState.copy(
                    openAlertDialog = false
                )
            }
        }

        fun saveTransactionData() {
            val transactionEntity = TransactionEntity(
                transactionDate = uiState.value.date,
                transactionDesc = uiState.value.desc,
                transactionCategory = uiState.value.category,
                transactionAmount = uiState.value.amount.toDouble(),
                isExpenses = uiState.value.isExpenses
            )

            if (uiState.value.transactionType == Constants.TransactionStatus.updateTransaction) {
                viewModelScope.launch(IO) {
                    if (itemID != null) {
                        transactionEntity.id = itemID
                    }
                    transactionRepository.update(transactionEntity)
                }.also {
                    _uiState.update { currentState->
                        currentState.copy(
                            navigationText = resourcesProvider.getString(R.string.record_update)
                        )
                    }.also {
                        firebaseAnalytics.logEvent(Constants.AnalyticsParameter.TRANSACTION_UPDATED,viewModelScope)
                        _navigateToHomeScreen.value = true
                    }
                }
            } else {
                viewModelScope.launch(IO) {
                    transactionRepository.insert(transactionEntity)
                }.also {
                    _uiState.update { currentState->
                        currentState.copy(
                            navigationText = resourcesProvider.getString(R.string.record_added)
                        )
                    }.also {
                        firebaseAnalytics.logEvent(Constants.AnalyticsParameter.TRANSACTION_ADDED,viewModelScope)
                        _navigateToHomeScreen.value = true
                    }
                }
            }
        }
}
