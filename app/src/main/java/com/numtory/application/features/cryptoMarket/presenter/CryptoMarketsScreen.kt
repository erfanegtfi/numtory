package com.numtory.application.features.cryptoMarket.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.components.TimerProgressBar
import com.numtory.application.composeUI.ItemNotFound
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.cryptoMarket.presenter.components.CryptoMarketAppBar
import com.numtory.application.features.cryptoMarket.presenter.components.CryptoMarketSearchAppBar
import com.numtory.application.features.cryptoMarket.presenter.components.CryptoPriceListItem
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun CryptoMarketPriceListScreen(
    navigator: DestinationsNavigator,
    viewModel: CryptoGlobalMarketPriceViewModel = koinViewModel()
) {
    var isSearching by remember { mutableStateOf(false) }

    val priceList by viewModel.priceState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            if (isSearching)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    CryptoMarketSearchAppBar(onSearch = {
                        viewModel.filter(it)
                    }) { isSearching = false }
                }
            else CryptoMarketAppBar { isSearching = true }

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
                            Box(modifier = Modifier.fillMaxSize().height(100.dp))
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
                                ItemNotFound()
                            }
                        else
                            itemsIndexed(items) { index, itemState ->
                                CryptoPriceListItem(
                                    itemState
//                                modifier = Modifier.background(if (index % 2 == 0) Gray0 else Color.White)
                                )
                            }
                    }

                    is ViewState.Failure -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("خطایی رخ داد!")
                            }
                        }
                    }
                }
            }
        }
    }
}