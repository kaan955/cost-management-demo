package com.demo.gidermanagement.ui.transactions

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.data.entity.TransactionEntity
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.theme.Color.Blue1000
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600
import com.demo.gidermanagement.ui.transactions.component.NumberPanel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AddTransactionScreen(
    navigateToScreen: (Screen) -> Unit,
){
    val addTransactionViewModel = hiltViewModel<AddTransactionViewModel>()
    val addTransactionUiState by addTransactionViewModel.uiState.collectAsState()


    val navigateToHomeScreen by addTransactionViewModel.navigateToHomeScreen.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1= navigateToHomeScreen){
        if (navigateToHomeScreen){
            Toast.makeText(context, addTransactionUiState.navigationText, Toast.LENGTH_SHORT).show()
            navigateToScreen.invoke(Screen.HomeScreen)
        }
    }

    AddTransactionContent(
        uiState = addTransactionUiState,
        updateDate = {addTransactionViewModel.updateDate(it)},
        updateDesc = { addTransactionViewModel.updateDesc(it) } ,
        updateCategory = { addTransactionViewModel.updateCategory() } ,
        updateAmount = { addTransactionViewModel.updateAmount(it) } ,
        saveTransactionData = { addTransactionViewModel.verifySave() },
        updateTransactionType = { addTransactionViewModel.updateTransactionType(it)},
        deleteTransaction = { addTransactionViewModel.deleteTransactionData(it)},
        setCategoryID = { addTransactionViewModel.setCategoryID(it)},
        updateFilterStatus = { addTransactionViewModel.updateFilterSheetStatus(it)},
        navigateToHome = { navigateToScreen(Screen.HomeScreen) },
        navigateToCategory = {navigateToScreen(Screen.CategoriesScreen) },
        closeWarningDialog = {addTransactionViewModel.closeWarningDialog()}
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    uiState: AddTransactionUiState,
    updateDate: (String) -> Unit,
    updateDesc: (String) -> Unit,
    updateCategory: () -> Unit,
    updateAmount: (String) -> Unit,
    saveTransactionData: () -> Unit,
    updateTransactionType: (Boolean) -> Unit,
    deleteTransaction: (TransactionEntity) -> Unit,
    setCategoryID: (Int) -> Unit,
    updateFilterStatus: (Boolean) -> Unit,
    navigateToHome: (Screen) -> Unit,
    navigateToCategory: (Screen) -> Unit,
    closeWarningDialog: () -> Unit,

) {
    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (uiState.transactionType == Constants.TransactionStatus.newTransaction)
                                stringResource(id = R.string.add_transaction_title)
                            else { stringResource(id = R.string.update_transaction_title)},
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = topAppBarColors(
                        containerColor = Blue600
                    ),
                    navigationIcon = {
                        IconButton(onClick = {navigateToHome.invoke(Screen.HomeScreen)}) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                tint = Color.White)
                        }
                    },
                    actions = {
                        if (uiState.transactionType == Constants.TransactionStatus.updateTransaction) {
                        IconButton(onClick = { isDeleteDialogOpen = true}) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.delete),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 8.dp, end = 16.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DatePicker(updateDate)

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.desc ?: "",
                            label = { Text(text = stringResource(id = R.string.title)) },
                            onValueChange = {
                                updateDesc(it)
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Black,
                                focusedIndicatorColor = Color.Black,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Black
                            ),
                            singleLine = true
                        )
                        CategoryPicker(
                            uiState = uiState,
                            updateFilterStatus = updateFilterStatus,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(id = R.string.income_type),
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Switch(
                                checked = uiState.isExpenses,
                                onCheckedChange = { updateTransactionType(!uiState.isExpenses) },
                                thumbContent = {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize)
                                    )
                                },
                                colors  = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Blue600,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor= Blue600,
                                    checkedBorderColor = Color.White,
                                    uncheckedBorderColor = Color.White,
                                    checkedIconColor = Color.Black,
                                    uncheckedIconColor = Color.Black
                                )
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = stringResource(id = R.string.outgoings_type),
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(Blue200),
                    verticalAlignment = Alignment.Bottom
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        NumberPanel(updateAmount, uiState.amount,saveTransactionData)
                    }

                }
            }
            DeleteDataAlertDialog(
                onDismiss = { isDeleteDialogOpen = false },
                uiState = uiState,
                deleteTransaction = deleteTransaction ,
                isDialogOpen = isDeleteDialogOpen
            )
        }
        if (uiState.showCategoryBottomSheet) {
            CategoryModalBottomSheet (
                uiState = uiState,
                updateCategory = updateCategory,
                setCategoryID = setCategoryID,
                updateFilterStatus = updateFilterStatus,
                navigateToCategory = navigateToCategory
            )
        }
        if (uiState.openAlertDialog) {
            AlertDialog(
                onDismissRequest = { closeWarningDialog() },
                text = {
                    Text(modifier = Modifier.padding(4.dp),
                        text = stringResource(id = R.string.add_new_category_warning),
                        fontWeight = FontWeight.SemiBold)
                    },
                confirmButton = {
                    Button(onClick = {closeWarningDialog()  }) {
                        Text(text = stringResource(id = R.string.dialog_confirm_text))
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
@Composable
fun DatePicker(updateDate: (String) -> Unit) {
    val date = remember { mutableStateOf(LocalDate.now())}
    val isOpen = remember { mutableStateOf(false)}

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .focusProperties { canFocus = false }
                .clickable { isOpen.value = true }
                .fillMaxWidth(),
            readOnly = true,
            enabled = false,
            value = date.value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            label = { Text("Date") },
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Black,
                disabledIndicatorColor = Color.Black

            ))
    }

    if (isOpen.value) {
        DatePickerDialog(
            onAccept = {
                isOpen.value = false // close dialog

                if (it != null) { // Set the date
                    date.value = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()

                    updateDate(
                        date.value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    )
                }
            },
            onCancel = {
                isOpen.value = false //close dialog
            }
        )
    }
}
@Composable
fun CategoryPicker(
    uiState: AddTransactionUiState,
    updateFilterStatus: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .focusProperties { canFocus = false }
                .clickable { updateFilterStatus(true) }
                .fillMaxWidth(),
            readOnly = true,
            enabled = false,
            value = uiState.category ?: stringResource(id = R.string.category_other),
            label = { Text(stringResource(id = R.string.category)) },
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Black,
                disabledIndicatorColor = Color.Black
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryModalBottomSheet(
    uiState: AddTransactionUiState,
    updateCategory: () -> Unit,
    setCategoryID: (Int) -> Unit,
    updateFilterStatus: (Boolean) -> Unit,
    navigateToCategory: (Screen) -> Unit
) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            ModalBottomSheet(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                onDismissRequest = {
                    updateFilterStatus(false)
                },
                sheetState = modalBottomSheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.White,
                tonalElevation = 16.dp,

                contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(AddTransactionDefaults.buttomCornerPercantage))
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.Center)
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(AddTransactionDefaults.categoryBottomSheetHeight)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(id = R.string.category_select),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { updateFilterStatus(false)}) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.close_icon),
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(8.dp))
                                .verticalScroll(scrollState)
                                .background(Blue200)
                                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 64.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            val selectedCategoryID = uiState.selectedCategoryID  // Burada güncel UI durumu alınıyor.
                            val radioOptions = uiState.categoryList

                            Column(modifier = Modifier.fillMaxHeight(
                                AddTransactionDefaults.halfWeight)
                                .padding(16.dp)) {
                                radioOptions.forEachIndexed { index, categoryItem ->
                                    Row(
                                        Modifier
                                            .fillMaxSize()
                                            .selectable(
                                                selected = (index == selectedCategoryID),
                                                onClick = { setCategoryID(index) }
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        RadioButton(
                                            selected = (index == selectedCategoryID),
                                            onClick = { setCategoryID(index) }
                                        )
                                        Text(text = categoryItem.categoryName ?: "")
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp),
                        ) {
                            AddCategoryButton(navigateToCategory = navigateToCategory)
                        }
                    }

                }
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Button(
                        modifier = Modifier
                            .weight(AddTransactionDefaults.halfWeight)
                            .height(56.dp),
                        shape = RoundedCornerShape(
                            AddTransactionDefaults.buttonRoundedCornerShape
                        ),
                        colors = ButtonColors(
                            containerColor = Blue1000,
                            contentColor = Color.White,
                            disabledContentColor = Color.LightGray,
                            disabledContainerColor = Color.DarkGray
                        ),
                        onClick = {
                            updateCategory()
                        }) {
                        Text(stringResource(id = R.string.category_select))
                    }
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}
@Composable
fun DeleteDataAlertDialog(
    onDismiss: () -> Unit,
    uiState: AddTransactionUiState,
    deleteTransaction: (TransactionEntity) -> Unit,
    isDialogOpen: Boolean
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = {
               onDismiss.invoke()
            },
            title = { Text(text = stringResource(id = R.string.delete_data)) },
            text = { Text(text = stringResource(id = R.string.delete_confirmation)) },
            confirmButton = {
                Button(
                    onClick = {
                        uiState.transactionEntity?.let { it1 -> deleteTransaction(it1) }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.notification_confirm),
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.notification_cancel),
                        color = Color.White
                    )
                }
            })
    }
}

@Composable
fun AddCategoryButton(
    navigateToCategory: (Screen) -> Unit
) {
    ElevatedButton(
        onClick = { navigateToCategory.invoke(Screen.CategoriesScreen) },
    ) {
        Text(text = stringResource(id = R.string.add_new_category))
    }
}

object AddTransactionDefaults {
    const val buttonRoundedCornerShape = 10
    const val halfWeight = 0.5f
    const val categoryBottomSheetHeight = 0.7f
    const val buttomCornerPercantage = 50

}
@Preview
@Composable
fun PreviewAddTransaction(){
    AddTransactionContent(
        uiState = AddTransactionUiState(
            "Test","Test","Test","0.0",true
        ),{},{},{},{},{},{},{},{},{},{},{},{}
    )
}
