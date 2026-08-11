package com.numtory.application.features.gold.presenter

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
import androidx.compose.material.icons.filled.ManageSearch
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.R
import com.numtory.application.composeUI.ErrorMessage
import com.numtory.application.composeUI.ItemNotFound
import com.numtory.application.composeUI.ObserveMarketLifecycle
import com.numtory.application.composeUI.ShowBottomSheet
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.cryptoMap
import com.numtory.application.features.cryptoMarket.domain.entities.metalMap
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.presenter.components.GoldAssetOptionsBottomSheetScreen
import com.numtory.application.features.gold.presenter.components.GoldPriceItem
import com.numtory.application.features.market.domain.entities.BestPrices
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.topMetalSymbols
import com.numtory.application.features.market.presenter.GetStickyHeader
import com.numtory.application.features.market.presenter.MarketsViewModel
import com.numtory.application.features.market.presenter.TokenListBottomSheetScreen
import com.numtory.application.features.market.presenter.components.GetMarketAverage
import com.numtory.application.features.market.presenter.components.MarketStatsRow
import com.numtory.application.features.market.presenter.components.table.MarketPriceHeader
import com.numtory.application.features.market.presenter.components.TimerProgressBar
import com.numtory.application.features.market.presenter.components.appbar.MarketTopBar
import com.numtory.application.features.market.presenter.components.appbar.TopBarAction
import com.numtory.application.ui.theme.CHART_SCRIPT
import com.ramcosta.composedestinations.generated.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppChartWebViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun GoldMarketList(
    navigator: DestinationsNavigator,
    viewModel: GoldMarketsViewModel = koinViewModel()
) {

    val priceList by viewModel.priceState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val pullToRefreshState = rememberPullToRefreshState()
    var showSheet by remember { mutableStateOf(false) }
    var showTokenList by remember { mutableStateOf(false) }
    val selectedToken = viewModel.selectedToken

    // Read here, in the composable body, so that a new price list recomposes the whole
    // screen and the best-price lookup below is re-evaluated along with it.
    val marketItems = (priceList as? ViewState.Success<List<GoldMarketPrice>>)?.data.orEmpty()
    val bestPrices = viewModel.getBestPrices()

//    printLogs(priceList)


    ObserveMarketLifecycle(
        lifecycleOwner = lifecycleOwner,
        onResume = viewModel::startTimer,
        onPause = viewModel::stopTimer
    )


    if (showSheet)
        ShowBottomSheet(onDismiss = { showSheet = false }) { modalBottomSheetState, hide ->
            GoldAssetOptionsBottomSheetScreen(
                viewModel.getUserExchanges(),
                viewModel.getSelectableExchanges(),
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
                topMetalSymbols,
                hide
            ) { token ->
                viewModel.selectToken(token)
                showTokenList = false
                viewModel.getPrices(token)
            }
        }

    Scaffold(
        topBar = {
            GetAppbar(selectedToken = viewModel.selectedToken.value) {
                showSheet = true
            }
        },
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
                        val (avgBuy, avgSell) = viewModel.getMarketAverage()
                        Column {
                            GetStickyHeader(
                                priceList = priceList,
                                timer = viewModel.timer.value,
                                selectedToken = selectedToken.value,
                                averageBuyPrice = avgBuy,
                                averageSellPrice = avgSell,
                                bestPrices = bestPrices,
                                onTokenClick = { showTokenList = true },
                                onChartClicked = {
                                    navigator.navigate(
                                        AppChartWebViewDestination(
                                            CHART_SCRIPT.replace(
                                                "{symbol_hear}",
                                                "brs:GOLD18IRT"
                                            ).trimIndent(),
                                        )
                                    )
                                },
                            )

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
                                )
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
                            val items = marketItems
                            if (items.isEmpty())
                                item {
                                    ItemNotFound()
                                }
                            else
                                itemsIndexed(items) { index, itemState ->
                                    GoldPriceItem(
                                        itemState,
                                        isBestSell = bestPrices.sellPrice != null &&
                                                itemState.finalSellPrice.toDoubleOrNull() == bestPrices.sellPrice,
                                        isBestBuy = bestPrices.buyPrice != null &&
                                                itemState.finalBuyPrice.toDoubleOrNull() == bestPrices.buyPrice,
                                    )
                                }
                        }

                        is ViewState.Failure -> {
                            item {
                                ErrorMessage()
                            }
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun GetAppbar(
    selectedToken: String,

    onSettingsClick: () -> Unit
) {
    return MarketTopBar(
        selectedToken = selectedToken,
        actions = {
            TopBarAction(
                icon = R.drawable.ic_setting,
                contentDescription = "Settings",
                onClick = onSettingsClick
            )
        }
    )

}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun GetStickyHeader(
    priceList: ViewState<List<GoldMarketPrice>>,
    timer: Int,
    averageBuyPrice: Double,
    averageSellPrice: Double,
    selectedToken: String,
    bestPrices: BestPrices,
    onTokenClick: () -> Unit,
    onChartClicked: () -> Unit,
) {
    return Column {

        Box(modifier = Modifier.height(2.dp))
        TimerProgressBar(timer)

        if (priceList is ViewState.Success<List<GoldMarketPrice>>) {

            GetMarketAverage(
                metalMap[selectedToken] ?: "",
                selectedToken,
                selectedToken,
                onTokenClicked = onTokenClick,
                onChartClicked = onChartClicked
            )

            MarketStatsRow(
                averageBuyPrice = averageBuyPrice,
                averageSellPrice = averageSellPrice,
                bestPrices = bestPrices,
            )
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