package com.numtory.application.features.market.presenter

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.R
import com.ramcosta.composedestinations.generated.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.numtory.application.composeUI.ShowBottomSheet
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.cryptoMap
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.topCryptoSymbols
import com.numtory.application.features.market.presenter.components.table.CryptoPriceItem
import com.numtory.application.features.market.presenter.components.GetMarketAverage
import com.numtory.application.features.market.presenter.components.table.MarketPriceHeader
import com.numtory.application.features.market.presenter.components.TimerProgressBar
import com.numtory.application.features.market.presenter.components.appbar.MarketTopBar
import com.numtory.application.ui.theme.CHART_SCRIPT
import com.numtory.application.composeUI.ObserveMarketLifecycle
import com.numtory.application.features.market.presenter.components.appbar.TopBarAction
import com.numtory.application.ui.theme.ThemeManager
import com.ramcosta.composedestinations.generated.destinations.AppChartWebViewDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

//@Destination<RootGraph>(start = true)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun MarketList(navigator: DestinationsNavigator, viewModel: MarketsViewModel = koinViewModel()) {

    val priceList by viewModel.priceState.collectAsStateWithLifecycle()
    val themeManager = koinInject<ThemeManager>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val pullToRefreshState = rememberPullToRefreshState()
    var showSheet by remember { mutableStateOf(false) }
    var showTokenList by remember { mutableStateOf(false) }
    val selectedToken = viewModel.selectedToken


    ObserveMarketLifecycle(
        lifecycleOwner = lifecycleOwner,
        onResume = viewModel::startTimer,
        onPause = viewModel::stopTimer
    )
    

    if (showSheet)
        ShowBottomSheet(onDismiss = { showSheet = false }) { modalBottomSheetState, hide ->
            AssetOptionsBottomSheetScreen(
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

    if (showTokenList)
        ShowBottomSheet(onDismiss = {
            showTokenList = false
        }) { modalBottomSheetState, hide ->
            TokenListBottomSheetScreen(
                topCryptoSymbols,
                hide
            ) { token ->
                viewModel.selectToken(token)
                showTokenList = false
                viewModel.getPrices(token)
            }
        }

    Scaffold(
        topBar = {
            MarketTopBar(
                selectedToken = viewModel.selectedToken.value,
                actions = {
                    IconButton(onClick = { themeManager.toggle() }) {
                        Icon(
                            imageVector = if (themeManager.isDarkTheme)
                                Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }

                    TopBarAction(
                        icon = R.drawable.ic_about,
                        contentDescription = "About",
                        onClick = {
                            navigator.navigate(AboutScreenDestination)
                        }
                    )

                    TopBarAction(
                        icon = R.drawable.ic_setting,
                        contentDescription = "Settings",
                        onClick = {
                            showSheet = true
                        }
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()//.padding(innerPadding)
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
                            if (priceList is ViewState.Success<List<MarketPrice>>) {

                                val (avgBuy, avgSell) = viewModel.getMarketAverage()
                                Box(modifier = Modifier.height(2.dp))
                                TimerProgressBar(viewModel.timer)
                                GetMarketAverage(
                                    avgBuy,
                                    avgSell,
                                    cryptoMap[selectedToken.value] ?: "",
                                    selectedToken.value,
                                    selectedToken.value,
                                    onChartClicked = {
                                        navigator.navigate(
                                            AppChartWebViewDestination(
                                                CHART_SCRIPT.replace(
                                                    "{symbol_hear}",
                                                    "nobitex_spot:${selectedToken.value}IRT"
                                                ).trimIndent(),
                                            )
                                        )
                                    },
                                    onTokenClicked = {
                                        showTokenList = true
                                    })


                            }
                            MarketPriceHeader(
                                sortField = viewModel.sortParams.sortField,
                                sortOrder = viewModel.sortParams.sortOrder,
                            ) { sortField, sortOrder ->
                                viewModel.sort(sortField, sortOrder)
                            }
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
                            val items = (priceList as ViewState.Success<List<MarketPrice>>).data
                            // best sell = highest price you get when selling;
                            // best buy = lowest price you pay when buying
                            val bestSell = items
                                .mapNotNull { it.finalSellPrice.toDoubleOrNull()?.takeIf { v -> v > 0 } }
                                .maxOrNull()
                            val bestBuy = items
                                .mapNotNull { it.finalBuyPrice.toDoubleOrNull()?.takeIf { v -> v > 0 } }
                                .minOrNull()
                            if (items.isEmpty())
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(300.dp)
                                    ) {
                                        Text(
                                            "موردی پیدا نشد",
                                            modifier = Modifier.align(alignment = Alignment.Center)
                                        )
                                    }
                                }
                            else
                                itemsIndexed(items) { index, itemState ->
                                    CryptoPriceItem(
                                        itemState,
                                        isBestSell = bestSell != null &&
                                                itemState.finalSellPrice.toDoubleOrNull() == bestSell,
                                        isBestBuy = bestBuy != null &&
                                                itemState.finalBuyPrice.toDoubleOrNull() == bestBuy,
                                    )
                                }
                        }

                        is ViewState.Failure -> {
                            item {
                                Text("خطایی رخ داد!")
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