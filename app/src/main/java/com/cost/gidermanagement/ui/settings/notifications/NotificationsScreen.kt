package com.demo.gidermanagement.ui.settings.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.settings.notifications.component.NotificationList
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600

@Composable
fun NotificationsScreen(
    onbackClicked: (Screen) -> Unit
){
    val notificationsViewModel = hiltViewModel<NotificationsViewModel>()
    val notificationsuiState by notificationsViewModel.uiState.collectAsState()

    NotificationsContent(
        uiState = notificationsuiState,
        onbackClicked = onbackClicked,
        deleteTransactionData = {notificationsViewModel.deleteNotification(it)},
        onConfirm = { title, date, time, repeatCount ->
            notificationsViewModel.saveNotification(title, date, time, repeatCount)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    uiState: NotificationsuiState,
    onbackClicked: (Screen) -> Unit,
    deleteTransactionData: (NotificationEntity) -> Unit,
    onConfirm: (title: String, date: String, time: String, repeatCount: Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue200)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.notifications_title),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue600
                    ),
                    navigationIcon = {
                        IconButton(onClick = { onbackClicked.invoke(Screen.SettingsScreen) }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                tint = Color.White
                            )
                        }
                    },
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = it.calculateBottomPadding() + 16.dp
                    )

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Blue200, RoundedCornerShape(16.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Blue200),
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(bottomEnd = 12.dp))
                                    .background(Blue600),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    text = stringResource(id = R.string.notifications_top_title),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                )
                            }
                            Row(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    top = 8.dp,
                                    bottom = 12.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.notifications_desc),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Blue200, RoundedCornerShape(16.dp))
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        NotificationList(
                            uiState.notificationList,
                            deleteTransactionData
                        )
                    }

                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp),
                        content = {
                            Image(
                                modifier = Modifier.clickable {
                                    showDialog = true
                                },
                                painter = painterResource(id = R.drawable.add_item),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        })
                }
            }
            if (showDialog) {
                DateTimeRepeatDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { title,date, time, repeatCount ->
                        onConfirm(title,date,time,repeatCount)
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun DateTimeRepeatDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, date: String, time: String, repeatCount: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var repeatCount by remember { mutableStateOf(1) }

    var isTitleError by remember { mutableStateOf(false) }
    var isDateError by remember { mutableStateOf(false) }
    var isTimeError by remember { mutableStateOf(false) }
    var isRepeatCountError by remember { mutableStateOf(false) }

    val dateRegex = Regex("""\d{2}/\d{2}/\d{4}""")
    val timeRegex = Regex("""\d{2}:\d{2}""")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.notifications_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Bildirim Adı Girişi
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        isTitleError = it.isEmpty()
                    },
                    label = { Text(stringResource(id = R.string.notification_name)) },
                    placeholder = { Text(stringResource(id = R.string.notification_name_hint)) },
                    isError = isTitleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isTitleError) {
                    Text(
                        text = stringResource(id = R.string.notification_name_error),
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                // Tarih Girişi
                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                        isDateError = it.isEmpty() || !dateRegex.matches(it)
                    },
                    label = { Text(stringResource(id = R.string.notification_date)) },
                    placeholder = { Text(stringResource(id = R.string.notification_date_hint)) },
                    isError = isDateError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isDateError) {
                    Text(
                        text = stringResource(id = R.string.notification_date_error),
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                // Saat Girişi
                OutlinedTextField(
                    value = time,
                    onValueChange = {
                        time = it
                        isTimeError = it.isEmpty() || !timeRegex.matches(it)
                    },
                    label = { Text(stringResource(id = R.string.notification_time))},
                    placeholder = { Text(stringResource(id = R.string.notification_time_hint))},
                    isError = isTimeError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isTimeError) {
                    Text(
                        text = stringResource(id = R.string.notification_time_hint),
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                // Tekrar Sayısı Girişi
                OutlinedTextField(
                    value = repeatCount.toString(),
                    onValueChange = {
                        repeatCount = it.toIntOrNull() ?: 1
                        isRepeatCountError = repeatCount <= 0
                    },
                    label = { Text(stringResource(id = R.string.notification_count)) },
                    placeholder = { stringResource(id = R.string.notification_count_hint)},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isRepeatCountError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isRepeatCountError) {
                    Text(
                        text = stringResource(id = R.string.notification_count_error),
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            val isConfirmEnabled = !isTitleError && !isDateError && !isTimeError && !isRepeatCountError &&
                    title.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()
            Button(
                onClick = { onConfirm(title, date, time, repeatCount) },
                enabled = isConfirmEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier.clip(RoundedCornerShape(2.dp))
            ) {
                Text(stringResource(id = R.string.notification_confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                ),
                modifier = Modifier.clip(RoundedCornerShape(2.dp))
            ) {
                Text(stringResource(id = R.string.notification_cancel))
            }
        }
    )
}



