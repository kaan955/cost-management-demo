package com.demo.gidermanagement.ui.settings.notifications


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
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.datasource.TransactionDao
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.ui.home.model.TransactionInfoItem
import com.demo.gidermanagement.ui.settings.model.SettingsItem
import com.demo.gidermanagement.ui.settings.preferences.PreferencesuiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class NotificationsuiState(
    var test: Boolean = false,
    var notificationList: List<NotificationEntity> = emptyList(),
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsuiState())
    val uiState: StateFlow<NotificationsuiState> = _uiState.asStateFlow()

    init {
        getAllNotifications()
    }



    private fun getAllNotifications() {
        viewModelScope.launch {
        _uiState.update { currentState ->
            currentState.copy(
                notificationList = transactionRepository.getAllNotifications()
            )
        }
        }
    }


    fun saveNotification(title: String, date: String, time: String, repeatCount: Int) {
        val notification = NotificationEntity(
            title = title,
            date = date,
            time = time,
            repeatCount = repeatCount
        )

        viewModelScope.launch {
            try {
                transactionRepository.insertNotification(notification)
                getAllNotifications()
            } catch (dataError: SQLiteException) {
                Timber.tag("NotificationViewModel").e(dataError, "Error saving notification")
            }
        }
    }

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            transactionRepository.deleteNotification(notification)
        }.also {
            getAllNotifications()
        }
    }
}
