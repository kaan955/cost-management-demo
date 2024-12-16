package com.demo.gidermanagement.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.common.model.BottomNavigationItem
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.theme.Color.Blue1100
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600
import com.demo.gidermanagement.ui.theme.Color.Gray100

@Composable
fun SettingsScreen(
    navigateToItem: (Screen) -> Unit,
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    SettingsContent(
        viewModel = settingsViewModel,
        navigateToItem = navigateToItem,
        excelProcess = {settingsViewModel.exportDataToExcel(it)},
        showInAppReview = { settingsViewModel.showInAppReview(it)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    viewModel: SettingsViewModel,
    navigateToItem: (Screen) -> Unit,
    excelProcess: (Context) -> Unit,
    showInAppReview: (Context) -> Unit
){

    val contactUsTitle = stringResource(id = R.string.contactUs)
    val evaluationTitle = stringResource(id = R.string.evaluation)

    val selectedNavigationIndex by rememberSaveable {
        mutableIntStateOf(2)
    }

    val openDialog = remember { mutableStateOf(false) }
    val openEvaluation  = remember { mutableStateOf(false) }
    val comingSoonDialog  = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val items = listOf(
        BottomNavigationItem(
            title = stringResource(id = R.string.homepage_bottom),
            icon = R.drawable.home,
            {navigateToItem(Screen.HomeScreen)}
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.list_bottom),
            icon = R.drawable.list,
            {navigateToItem(Screen.DetailListScreen)}
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.settings_bottom),
            icon = R.drawable.settings,
            {}
        )
    )

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
                            stringResource(id = R.string.settings_title),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue600
                    ),
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Blue600,
                    contentColor = Color.Transparent
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Gray100,
                                unselectedIconColor = Blue1100,
                                selectedTextColor = Gray100,
                                unselectedTextColor = Blue1100,
                                indicatorColor = Color.Transparent
                            ),
                            selected = selectedNavigationIndex == index,
                            label = { Text(item.title ?: "") },
                            onClick = { item.onClick.invoke() },
                            icon = {
                                Icon(
                                    painterResource(id = item.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                )
                            })
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(SettingsDefaults.settingsPremiumPartHeight),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*
                    ElevatedButton(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(.6f),
                        shape = RoundedCornerShape(20),
                        colors = ButtonColors(
                            containerColor = Blue600,
                            contentColor = Color.White,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Black
                        ),
                        onClick = {comingSoonDialog.value = true}
                    ) {
                        Text(
                            text = stringResource(id = R.string.premium_tab), maxLines = 1,
                            fontWeight =FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.premium),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 8.dp)
                        )
                    }*/
                }

                Row(
                    modifier = Modifier
                        .fillMaxHeight(SettingsDefaults.settingsListHeight)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
                        .background(Blue200)
                )
                {
                    val settingsList by viewModel.settingsList

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            columns = GridCells.Fixed(Constants.NumberConstants.THREE),

                            // content padding
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                top = 8.dp,
                                end = 12.dp,
                                bottom = 16.dp
                            ),
                            content = {
                                items(settingsList.size) { index ->
                                    ElevatedButton(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        shape = RoundedCornerShape(SettingsDefaults.settingsDefaultRounded),
                                        colors = ButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black,
                                            disabledContainerColor = Color.LightGray,
                                            disabledContentColor = Color.Black
                                        ),
                                        onClick = {
                                        if (settingsList[index].navigationScreen != Screen.SettingsScreen) {
                                            navigateToItem(settingsList[index].navigationScreen)
                                        }else {
                                            if(settingsList[index].title == contactUsTitle) {
                                                openDialog.value = true
                                            } else if(settingsList[index].title == evaluationTitle) {
                                                openEvaluation.value = true
                                            }
                                            else if(settingsList[index].title ==
                                                context.getString(R.string.excel_tab)
                                                ) {
                                                excelProcess.invoke(context)
                                            }
                                            else if(settingsList[index].title ==
                                                context.getString(R.string.password_tab)
                                                ) {
                                                comingSoonDialog.value = true
                                            }
                                            else if(settingsList[index].title ==
                                                context.getString(R.string.backup_tab)
                                                ) {
                                                comingSoonDialog.value = true
                                            }
                                            else if(settingsList[index].title ==
                                                context.getString(R.string.aboutUs_tab)
                                                ) {
                                                comingSoonDialog.value = true
                                            }
                                        }
                                }
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                painterResource(id = settingsList[index].icon),
                                                contentDescription = null,
                                                modifier = Modifier.size(28.dp),
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = settingsList[index].title ?: "",
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                maxLines = 1, overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }

                }

            }
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    title = {
                        Text(text = stringResource(id = R.string.dialog_contact_us_text))
                    },
                    text = {
                        Column {
                            Text(text = stringResource(id = R.string.dialog_contact_us_desc))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Email: app.costmanagement@outlook.com",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = TextDecoration.Underline, // Altını çizme
                                modifier = Modifier.clickable {
                                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:app.costmanagement@outlook.com")
                                    }
                                    context.startActivity(emailIntent)
                                })
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { openDialog.value = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue600
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = stringResource(id = R.string.dialog_confirm_text), color = Color.White)
                        }
                    }
                )
            }
            if (openEvaluation.value) {
                AlertDialog(
                    onDismissRequest = { openEvaluation.value = false },
                    title = {
                        Text(stringResource(id = R.string.dialog_evaluation_text))
                    },
                    text = {
                        var rating by remember { mutableStateOf(0) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                for (i in
                                Constants.NumberConstants.ONE..Constants.NumberConstants.FIVE) {
                                    Icon(
                                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = "Star $i",
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clickable { rating = i },
                                        tint = if (i <= rating) Blue1100 else Color.LightGray
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showInAppReview(context)
                                openEvaluation.value = false
                            }){
                                Text(text = stringResource(id = R.string.dialog_confirm_send_text))
                            }
                    }
                )
            }
            if (comingSoonDialog.value) {
                AlertDialog(
                    onDismissRequest = { comingSoonDialog.value = false},
                    title = {
                        Text(text = stringResource(id = R.string.comingSoon_title))
                    },
                    text = {
                        Text(text = stringResource(id = R.string.comingSoon_desc))
                    },
                    confirmButton = {
                        TextButton(onClick = { comingSoonDialog.value = false }) {
                            Text(text = stringResource(id = R.string.dialog_confirm_text))
                        }
                    }
                )
            }

        }
    }
}

object SettingsDefaults {
    const val settingsDefaultRounded = 20
    const val settingsListHeight = 0.7f
    const val settingsPremiumPartHeight = 0.3f
}
