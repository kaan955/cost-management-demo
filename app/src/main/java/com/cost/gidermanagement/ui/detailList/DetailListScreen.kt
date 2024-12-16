package com.demo.gidermanagement.ui.detailList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.model.BottomNavigationItem
import com.demo.gidermanagement.data.entity.TransactionEntity
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.detailList.component.BarListGraph
import com.demo.gidermanagement.ui.detailList.component.CategoryListGraph
import com.demo.gidermanagement.ui.detailList.component.DetailList
import com.demo.gidermanagement.ui.detailList.component.InformItem
import com.demo.gidermanagement.ui.detailList.model.BarChartData
import com.demo.gidermanagement.ui.detailList.model.CategoryFilter
import com.demo.gidermanagement.ui.theme.Color.Blue1000
import com.demo.gidermanagement.ui.theme.Color.Blue1100
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600
import com.demo.gidermanagement.ui.theme.Color.Gray100
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailListScreen(
    navigateToBottomItem: (Screen) -> Unit,
    navigateToTransaction: (Int) -> Unit,
) {

    val detailListViewModel = hiltViewModel<DetailListViewModel>()
    val detailListUiState by detailListViewModel.uiState.collectAsState()
    val currency = detailListViewModel.selectedCurrency.value

    DetailListContent(
        uiState = detailListUiState,
        currency = currency,
        navigateToBottomItem = navigateToBottomItem,
        navigateToTransaction = navigateToTransaction,
        deleteTransactionData = { detailListViewModel.deleteTransactionData(it) },
        filterSheetStatus = {detailListViewModel.updateFilterSheetStatus(it) },
        setPaymentType = { detailListViewModel.setPaymentType(it) },
        categorySelection = {detailListViewModel.categorySelection(it)},
        filteredTransaction =  {detailListViewModel.filteredTransaction()}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailListContent(
    uiState: DetailListUiState,
    currency: String? = "",
    navigateToBottomItem: (Screen) -> Unit,
    navigateToTransaction: (Int) -> Unit,
    deleteTransactionData: (TransactionEntity) -> Unit,
    filterSheetStatus: (Boolean) -> Unit,
    setPaymentType: (Int) -> Unit,
    categorySelection: (CategoryFilter) -> Unit,
    filteredTransaction: () -> Unit
    ) {

    var selectedNavigationIndex by rememberSaveable {
        mutableIntStateOf(1)
    }


    val items = listOf(
        BottomNavigationItem(
            title = stringResource(id = R.string.homepage_bottom),
            icon = R.drawable.home,
            { navigateToBottomItem.invoke(Screen.HomeScreen) }
        ),
        BottomNavigationItem(
            title =  stringResource(id = R.string.list_bottom),
            icon = R.drawable.list,
            {}
        ),
        BottomNavigationItem(
            title =  stringResource(id = R.string.settings_bottom),
            icon = R.drawable.settings,
            { navigateToBottomItem.invoke(Screen.SettingsScreen) }
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
                            stringResource(id = R.string.list_title),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue600
                    ),
                    actions = {
                            IconButton(onClick = {filterSheetStatus(true)}) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = ImageVector.vectorResource(R.drawable.filtre_icon),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                    }
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
                            onClick = { item.onClick.invoke()},
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    val listState = rememberLazyListState()
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(Unit) {
                        delay(DetailListDefaults.initDelay)
                        while (true) {
                            coroutineScope.launch {
                                val currentItem = listState.firstVisibleItemIndex
                                val nextItem = (currentItem + 1) %
                                        DetailListDefaults.infiniteLoopListSize // Sonsuz döngü için % list.size
                                listState.animateScrollToItem(nextItem)
                            }
                            delay(DetailListDefaults.delayBetweenItems) // 6 saniyelik gecikme
                        }
                    }

                        LazyRow(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                colors = CardColors(
                                    containerColor = Blue200,
                                    contentColor = Color.Transparent,
                                    disabledContainerColor = Blue200,
                                    disabledContentColor = Color.Transparent
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillParentMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(bottomEnd = 12.dp))
                                            .background(Blue600),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            ),
                                            text = stringResource(id = R.string.list_balance_graph_title),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = Color.White,
                                        )
                                    }
                                    BarListGraph(balanceGraphList = listOf(
                                        BarChartData(stringResource(id = R.string.income_type),
                                            uiState.incomePercentage
                                        ),
                                        BarChartData(stringResource(id = R.string.outgoings_type),
                                            uiState.expensesPercantage
                                        )
                                    ) )
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                colors = CardColors(
                                    containerColor = Blue200,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Blue200,
                                    disabledContentColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(bottomEnd = 12.dp))
                                        .background(Blue600),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        text = stringResource(id = R.string.list_category_graph_title),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = Color.White,
                                    )
                                }
                                Column(
                                    modifier = Modifier.fillParentMaxWidth()
                                ) {
                                    CategoryListGraph(uiState.categoryGraphList)
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                colors = CardColors(
                                    containerColor = Blue200,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Blue200,
                                    disabledContentColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(bottomEnd = 12.dp))
                                        .background(Blue600),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        text = stringResource(id = R.string.list_statictics_graph_title),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = Color.White,
                                    )
                                }
                                Column(
                                    modifier = Modifier.fillParentMaxWidth()
                                ) {
                                    InformItem(uiState = uiState)
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    DetailList(
                        navigateToTransaction,
                        financialList = uiState.transactionList,
                        deleteTransactionData = deleteTransactionData,
                        currency = currency
                    )
                }
            }
        }
        if (uiState.showFilterSheet) {
            FilterModalBottomSheet(
                filterSheetStatus = filterSheetStatus,
                uiState = uiState,
                setPaymentType = setPaymentType,
                categorySelection = categorySelection,
                filteredTransaction = filteredTransaction
            )
        }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterModalBottomSheet(
    filterSheetStatus: (Boolean) -> Unit,
    uiState: DetailListUiState,
    setPaymentType: (Int) -> Unit,
    categorySelection: (CategoryFilter) -> Unit,
    filteredTransaction: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
            ModalBottomSheet(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                onDismissRequest = {
                    filterSheetStatus(false)
                },
                sheetState = modalBottomSheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.White,
                tonalElevation = 16.dp,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(DetailListDefaults.defaultHalfPercentage))
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.Center)
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(DetailListDefaults.filterModelHeight)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(id = R.string.filter_title),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { filterSheetStatus(false) }) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.close_icon),
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxHeight(DetailListDefaults.filterModelHeight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Blue200)
                                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            val radioOptions = listOf(stringResource(id = R.string.income_type),
                                    stringResource(id = R.string.outgoings_type),
                                    stringResource(id = R.string.all_type))
                            val selectedOption = radioOptions[uiState.selectedRadioPaymentType]


                            Text(
                                text =  stringResource(id = R.string.filter_pay_type),
                                fontWeight = FontWeight.SemiBold
                            )
                            radioOptions.forEach { text ->
                                Row(
                                    Modifier
                                        .selectable(
                                            selected = (text == selectedOption),
                                            onClick = {
                                                setPaymentType(radioOptions.indexOf(text))
                                            }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    RadioButton(
                                        selected = (text == selectedOption),
                                        onClick = { setPaymentType(radioOptions.indexOf(text))}
                                    )
                                    Text(
                                        text = text,
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Blue200)
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    item {
                                        Text(
                                            text = stringResource(id = R.string.filter_category),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    items(uiState.filterCategoryList, key = { listItem ->
                                        listItem.categoryName }) { item ->
                                        CheckList(
                                            item,
                                            categorySelection
                                        )
                                    }
                                }
                            }

                    }
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Absolute.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(DetailListDefaults.halfWeight)
                                .height(56.dp),
                            shape = RoundedCornerShape(DetailListDefaults.defaultRoundCornerPercentage),
                            colors = ButtonColors(
                                containerColor = Blue1000,
                                contentColor = Color.White,
                                disabledContentColor = Color.LightGray,
                                disabledContainerColor = Color.DarkGray
                            ),
                            onClick = {
                                filteredTransaction()
                            }) {
                            Text(stringResource(id = R.string.filter_button))
                        }
                    }
                }
            }
    }
}


object DetailListDefaults {
    const val defaultRoundCornerPercentage = 10
    const val defaultHalfPercentage = 50
    const val halfWeight = .5f
    const val initDelay = 4000L
    const val delayBetweenItems = 6000L
    const val filterModelHeight = .7f
    const val infiniteLoopListSize = 3
}


@Composable
fun CheckList(
    categoryItem: CategoryFilter,
    categorySelection: (CategoryFilter) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = categoryItem.checked,
            onCheckedChange = { checked ->
                categorySelection(CategoryFilter(categoryItem.categoryName, checked))
            }
        )
        Text(categoryItem.categoryName)}
    }

@Composable
@Preview
fun DetailListPreview() {
    DetailListContent(
        uiState = DetailListUiState(
            desc = null,
           ArrayList()
        ),
        "",
        {},{},{},{},{},{},{}
    )
}
