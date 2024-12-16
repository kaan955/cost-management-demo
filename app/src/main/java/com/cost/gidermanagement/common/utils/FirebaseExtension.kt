package com.demo.gidermanagement.common.utils

import android.os.Bundle
import com.demo.gidermanagement.common.constant.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Extension function to log visit events
fun FirebaseAnalytics.logEvent(eventName: String, viewModelScope:kotlinx.coroutines.CoroutineScope) {
    viewModelScope.launch(Dispatchers.IO) {
        this@logEvent.logEvent(eventName, null)

    }
}
fun FirebaseAnalytics.logTransactionEvent(transactionStatus: String, viewModelScope:kotlinx.coroutines.CoroutineScope) {
    val bundle = Bundle().apply {
        putString(Constants.AnalyticsParameter.TRANSACTION_PROCESS, transactionStatus)
    }
    viewModelScope.launch(Dispatchers.IO) {
        this@logTransactionEvent.logEvent(Constants.Analytics.TRANSACTION_STATUS_EVENT, bundle)

    }
}
