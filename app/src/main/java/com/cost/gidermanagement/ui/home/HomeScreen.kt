package com.demo.gidermanagement.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.model.BottomNavigationItem
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.home.component.HistoryDataList
import com.demo.gidermanagement.ui.home.component.TopBalanceInfo
import com.demo.gidermanagement.ui.home.component.UpcomingTransactions
import com.demo.gidermanagement.ui.theme.Color.Blue1100
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600
import com.demo.gidermanagement.ui.theme.Color.Gray100
import com.demo.gidermanagement.ui.theme.GiderYönetimiTheme


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    navigateToTransaction: (Int) -> Unit,
    navigateToBottomItem: (Screen) -> Unit,
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val currency = homeViewModel.selectedCurrency.value

    HomeContent(
        uiState = homeUiState,
        currency = currency,
        navigateToTransaction = navigateToTransaction,
        filterChange = { homeViewModel.updateHomePageFilter(it) },
        navigateToBottomItem = navigateToBottomItem
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    currency: String,
    navigateToTransaction: (Int) -> Unit,
    filterChange: (Int) -> Unit,
    navigateToBottomItem: (Screen) -> Unit,
) {
    val selectedNavigationIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val items = listOf(
        BottomNavigationItem(
            title = stringResource(id = R.string.homepage_bottom),
            icon = R.drawable.home,
            {}
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.list_bottom),
            icon = R.drawable.list,
            {navigateToBottomItem(Screen.DetailListScreen)}
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.settings_bottom),
            icon = R.drawable.settings,
            {navigateToBottomItem(Screen.SettingsScreen)}
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
                            stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue600
                    )
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
                            label = { Text(item.title?: "") },
                            onClick = { item.onClick.invoke() },
                            icon = { Icon(
                                painterResource(id = item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            ) })
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
                    .padding(HomeDefaults.DefaultPadding)
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                TopBalanceInfo(
                    uiState.filterArray,
                    uiState.selectedHomePageFilter,
                    filterChange,
                    uiState.transactionGraphList,
                    uiState
                )
                Spacer(modifier = Modifier.height(6.dp))
                UpcomingTransactions(modifier = Modifier.fillMaxHeight(HomeDefaults.upComingHeight),
                    navigateToTransaction, futureFinancialList = uiState.futureTransactionList, currency = currency
                )
                Spacer(modifier = Modifier.height(2.dp))
                HistoryDataList(
                    navigateToTransaction, financialList = uiState.filteredTransactionList,currency = currency
                )
            }
        }
    }
}



object HomeDefaults {
    val DefaultPadding = PaddingValues(
        start = 8.dp,
        end = 8.dp,
    )
    const val upComingHeight = 0.3f
    const val spacerBetweenContents = 2
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    GiderYönetimiTheme {
        HomeContent(
            uiState = HomeUiState(),
            "",
            {},
            {},{}
        )
    }
}
