package com.numtory.application.features.cryptoMarket.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.BuildConfig
import com.numtory.application.R
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.presenter.components.GetMarketAverage
import com.numtory.application.features.market.presenter.components.MarketPriceHeader
import com.numtory.application.features.market.presenter.components.TimerProgressBar
import com.numtory.application.ui.theme.CHART_SCRIPT
import com.numtory.application.ui.theme.Primary
import com.ramcosta.composedestinations.generated.destinations.AppChartWebViewDestination
import com.ramcosta.composedestinations.generated.destinations.AppChartWebViewDestination.invoke
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun CryptoListScreen(
    navigator: DestinationsNavigator,
    viewModel: CryptoGlobalMarketPriceViewModel = koinViewModel()
) {

    val priceList by viewModel.priceState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(90.dp).background(Primary),
                title = {
                    Text(
                        text = "قیمت رمز ارزها",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = priceList is ViewState.Loading,
            onRefresh = { viewModel.getPrices() },
            state = pullToRefreshState,
            modifier = Modifier

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(), // Only top padding
                        start = innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Rtl)
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                stickyHeader {
                    Column {
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                        )
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
                        val items = (priceList as ViewState.Success<List<CryptoMarketPrice>>).data
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
                                CryptoListItem(
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

@Composable
fun CryptoListItem(crypto: CryptoMarketPrice) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,


        ) {
        // Left section: Image, Name & Symbol
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),

            ) {
            // Crypto Icon/Image
            MyImageLoader(
                BuildConfig.CRYPTO_ICON_URL.replace(
                    "{icon}",
                    crypto.symbol?.lowercase() ?: ""
                )
            )


            // Name and Symbol
            Column {
                Text(
                    text = crypto.name ?: "",
                    fontSize = 14.sp,

                    )
                Text(
                    text = crypto.symbol?.uppercase() ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Right section: Price and Change
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${String.format("%,.2f", crypto.price?.toFloat())}",
                fontSize = 14.sp,
            )

            // Price Change with color indicator
            val changeColor = if (crypto.dayChange >= 0)
                Color(0xFF4CAF50) else Color(0xFFF44336)
            val changeSymbol = if (crypto.dayChange >= 0) "▲" else "▼"

            Text(
                text = "${
                    String.format(
                        "%.2f",
                        kotlin.math.abs(crypto.dayChange)
                    )
                }% $changeSymbol",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = changeColor
            )
        }
    }
}