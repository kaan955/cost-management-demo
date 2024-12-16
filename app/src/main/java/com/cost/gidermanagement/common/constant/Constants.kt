package com.demo.gidermanagement.common.constant

import com.demo.gidermanagement.ui.settings.preferences.data.LanguageItem

object Constants  {

    object NumberConstants {
        const val ZERO = 0
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
    }

    object NumberOfDayCountConstants {
        const val TODAY = 0
        const val LAST_THREE_DAYS = 3
        const val LAST_SEVEN_DAYS = 7
        const val LAST_ONE_MONTH = 30
        const val LAST_THREE_MONTHS = 90
        const val LAST_ONE_YEAR = 365

    }

    object TransactionItemStatus {
        const val newTransactionItem = -1
    }

    object NavigationParameter {
        const val TRANSACTION_ID = "transactionID"
    }
    object TransactionStatus {
        const val newTransaction = 0
        const val updateTransaction = 1
    }

    object SharedPreferences {
        const val shared_pref_name = "com.demo.gidermanagement.sharedpref"
        const val isDarkMode = "IS_DARK_MODE"
        const val appLanguage = "APP_LANGUAGE"
        const val selectedCurrency = "SELECTED_CURRENCY"
        const val updateCategories = "UPDATE_CATEGORIES"
    }

    object PreferencesScreen {
        val languageItems = listOf(
            LanguageItem("English","en"),
            LanguageItem("Türkçe","tr"),
            LanguageItem("Español","es"),
            LanguageItem("Deutsch","de"))
        val currencies = listOf("$", "€", "₺", "£")
    }

    object AddTransactionScreen {
        const val defaultCategoryIndex = 4 // other
    }
    object SettingsScreen {
        const val  SettingsItemCount = 6
    }

    object HomeScreen {
        const val defaultGraphPercentage = 50.0
        const val percentageMultiplier = 100
    }

    object Analytics {
        const val VISIT_EVENT =  "visit_event"
        const val TRANSACTION_STATUS_EVENT = "transaction_status_event"

    }
    object AnalyticsParameter {
        const val VISIT = "visit"
        const val HOME_VISIT = "home_visited"
        const val DETAIL_LIST_VISIT = "detailList_visited"
        const val SETTINGS_VISIT = "settings_visited"
        const val ADD_TRANSACTION_VISIT = "addTransaction_visited"
        const val TRANSACTION_PROCESS = "transaction_status"
        const val TRANSACTION_ADDED = "transaction_added"
        const val TRANSACTION_DELETED = "transaction_deleted"
        const val TRANSACTION_UPDATED = "transaction_updated"
    }
}
