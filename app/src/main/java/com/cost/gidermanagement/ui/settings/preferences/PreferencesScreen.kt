package com.demo.gidermanagement.ui.settings.preferences

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.settings.preferences.data.LanguageItem
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600

@Composable
fun PreferencesScreen(
    navigation: (Screen) -> Unit
){
    val preferencesViewModel = hiltViewModel<PreferencesViewModel>()
    val navigateToHomeScreen by preferencesViewModel.navigateToHomeScreen.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1= navigateToHomeScreen){
        if (navigateToHomeScreen){
            Toast.makeText(context, context.getString(R.string.preferences_data_delete), Toast.LENGTH_SHORT).show()
            navigation.invoke(Screen.HomeScreen)
        }
    }

SettingsContent(
    viewModel = preferencesViewModel,
    navigation = navigation,
    saveLanguage = { languageItem: LanguageItem -> preferencesViewModel.saveLanguage(languageItem)},
    saveCurrency = {preferencesViewModel.saveCurrency(it)},
    deleteAllData = {preferencesViewModel.deleteAllData()}
)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    viewModel: PreferencesViewModel,
    navigation: (Screen) -> Unit,
    saveLanguage: (LanguageItem) -> Unit,
    saveCurrency: (String) -> Unit,
    deleteAllData: () -> Unit
) {
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
                            stringResource(id = R.string.preferences_title),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue600
                    ),
                    navigationIcon = {
                        IconButton(onClick = {navigation.invoke(Screen.SettingsScreen)}) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                tint = Color.White
                            )
                        }
                    },
                )
            },
        ) { surfacePadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = surfacePadding.calculateTopPadding() + 8.dp, start = 8.dp, end = 8.dp)

            ) {
                val appLanguage by viewModel.appLanguage
                val selectedCurrency by viewModel.selectedCurrency
                val isDarkTheme by viewModel.isDarkMode

                var isDialogOpen by remember { mutableStateOf(false) }
                isDarkTheme.toString() // it will be handled SOON

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blue600, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Dil Seçimi Satırı
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(id = R.string.preference_language), modifier = Modifier.weight(1f))
                        LanguageDropdown(selectedLanguage = appLanguage) { newLanguageItem:LanguageItem ->
                            saveLanguage(newLanguageItem)
                        }
                    }

                    // Tema Seçimi Satırı
                    /*Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(id = R.string.preference_mode), modifier = Modifier.weight(1f))
                        Switch(checked = isDarkTheme, onCheckedChange = { saveTheme(it) })
                    }*/

                    // Para Birimi Seçimi Satırı
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(id = R.string.preference_currency), modifier = Modifier.weight(1f))
                        CurrencyDropdown(selectedCurrency) { saveCurrency(it) }
                    }

                    Row( modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        ) {
                        Button(
                            onClick = { isDialogOpen = true },
                            modifier = Modifier
                                .fillMaxWidth(
                                    PreferencesDefaults.buttonDeleteDataWidth
                                ),
                            shape = RoundedCornerShape(
                                PreferencesDefaults.buttonRoundedCornerDefaults
                            )
                        ) {
                            Text(text = stringResource(id = R.string.preference_deleteALL))
                        }
                    }
                }
                ClearDataAlertDialog(
                    onConfirm = {
                        deleteAllData.invoke()
                                },
                    onDismiss = { isDialogOpen = false },
                    isDialogOpen = isDialogOpen
                )
            }
        }
    }
    BackHandler {
        navigation.invoke(Screen.SettingsScreen)
    }
}
        @Composable
        fun LanguageDropdown(
            selectedLanguage: String, onLanguageSelected: (LanguageItem) -> Unit
        ) {
            var expanded by remember { mutableStateOf(false) }
            val languages = Constants.PreferencesScreen.languageItems

            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(text = selectedLanguage)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    languages.forEach { language ->
                        DropdownMenuItem(onClick = {
                            onLanguageSelected(language)
                            expanded = false
                        },text =  {Text(text = language.languageTitle ?: "English")})
                    }
                }
            }
        }

        @Composable
        fun CurrencyDropdown(selectedCurrency: String, onCurrencySelected: (String) -> Unit) {
            var expanded by remember { mutableStateOf(false) }
            val currencies = Constants.PreferencesScreen.currencies

            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(text = selectedCurrency)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(onClick = {
                            onCurrencySelected(currency)
                            expanded = false
                        }, text = { Text(text = currency) })
                    }
                }
            }
        }

@Composable
fun ClearDataAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDialogOpen: Boolean
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(id = R.string.delete_contents)) },
            text = { Text(stringResource(id = R.string.delete_contents_desc)) },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text( text = stringResource(id = R.string.notification_confirm),)
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text( text = stringResource(id = R.string.notification_cancel))
                }
            }
        )
    }
}

object PreferencesDefaults {
    const val buttonRoundedCornerDefaults = 8
    const val buttonDeleteDataWidth = .8f
}

