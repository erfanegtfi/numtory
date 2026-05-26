package com.numtory.application.features.gold

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.R
import com.numtory.application.composeUI.ShowBottomSheet
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.usecase.FilterGoldParams
import com.numtory.application.features.gold.domain.usecase.SortGoldParams
import com.numtory.application.features.gold.presenter.GoldMarketsViewModel
import com.numtory.application.features.gold.presenter.components.GoldAssetOptionsBottomSheetScreen
import com.numtory.application.features.gold.presenter.components.GoldPriceItem
import com.numtory.application.features.market.presenter.components.GetMarketAverage
import com.numtory.application.features.market.presenter.components.MarketPriceHeader
import com.numtory.application.features.market.presenter.components.TimerProgressBar
import com.numtory.application.ui.theme.Primary
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun GoldMarketList(navigator: DestinationsNavigator, viewModel: GoldMarketsViewModel = koinViewModel()) {

    val priceList by viewModel.priceState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val pullToRefreshState = rememberPullToRefreshState()
    var sortParam by remember { mutableStateOf(SortGoldParams()) }
    var filterParam by remember { mutableStateOf(FilterGoldParams()) }
    var showSheet by remember { mutableStateOf(false) }

//    printLogs(priceList)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.startTimer()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.stopTimer()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

//    LaunchedEffect( 12) {
//            viewModel.getPrices()
//    }


    if (showSheet)
        ShowBottomSheet(onDismiss = { showSheet = false }) { modalBottomSheetState, hide ->
            GoldAssetOptionsBottomSheetScreen(
                viewModel.getUserExchanges(),
                viewModel.getActiveExchangesInfo(),
                viewModel.getAddFee(),
                hide
            ) { exchanges, addFee ->
                val checkedExchanges = exchanges.filter { it.value }.keys.toList()
                viewModel.saveDisplayExchanges(checkedExchanges)
                viewModel.saveAddFee(addFee)
                showSheet = false
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(90.dp).background(Primary),
                colors = TopAppBarDefaults.topAppBarColors(// Use 'surface' instead of 'primary' for the app bar background
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Box(modifier = Modifier.fillMaxSize()) {
//                        Text(
//                            text = "USDT",
//                            modifier = Modifier
//                                .align(Alignment.CenterEnd)
//                                .padding(end = 18.dp),
//                            style = MaterialTheme.typography.titleLarge
//                        )
                        Text(
                            text = "توکن چند",
                            modifier = Modifier.align(Alignment.CenterStart),
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSheet = true
                    }) {
                        Icon(
                            modifier = Modifier.padding(9.dp),
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = "Menu"
                        )
                    }

                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {  innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize()//.padding(innerPadding)
                .padding(
                    top = innerPadding.calculateTopPadding(), // Only top padding
                    start = innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Rtl)
                )
        ) {
            PullToRefreshBox(
                isRefreshing = priceList is ViewState.Loading,
                onRefresh = { viewModel.getPrices() },
                state = pullToRefreshState,
                modifier = Modifier
                    .weight(1f)

            ) {
                LazyColumn {

                    stickyHeader {
                        Column {
                            if (priceList is ViewState.Success<List<GoldMarketPrice>>) {

                                val (avgBuy, avgSell) = viewModel.getMarketAverage()

                                Box(modifier = Modifier.clickable {

                                },) {
                                    GetMarketAverage(avgBuy, avgSell, "طلا آب شده (18 عیار)", "Gold 18", R.drawable.xaut)
                                }
                            }
                            MarketPriceHeader(
                                sortField = sortParam.sortField,
                                sortOrder = sortParam.sortOrder,
                            ) {sortField, sortOrder ->
                                viewModel.sort(sortField, sortOrder)
                            }
                            TimerProgressBar(viewModel.timer)
                        }

                    }

                    when (priceList) {
                        is ViewState.Init -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height(100.dp)
                                ) {
                                    Text("")
                                }
                            }

                        }

                        is ViewState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillParentMaxSize()  // Takes full parent size
                                        .wrapContentSize(), // Centers content
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is ViewState.Success -> {
                            val items = (priceList as ViewState.Success<List<GoldMarketPrice>>).data
                            if (items.isEmpty())
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize().height(300.dp)
                                    ){
                                        Text("موردی پیدا نشد", modifier = Modifier.align(alignment = Alignment.Center))
                                    }
                                }
                            else
                                itemsIndexed(items) { index, itemState ->
                                    GoldPriceItem(
                                        itemState
//                                modifier = Modifier.background(if (index % 2 == 0) Gray0 else Color.White)
                                    )
                                }
                        }

                        is ViewState.Failure -> {
                            item {
                                Text("خطایی رخ داد!")
//                                Text("Error: ${(priceList as ViewState.Failure).error.message}")
                            }
                        }

                    }
                }
            }


        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    MyApplicationTheme {
//        MarketList()
//    }
}