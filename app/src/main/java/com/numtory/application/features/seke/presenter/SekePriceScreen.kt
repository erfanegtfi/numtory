package com.numtory.application.features.seke.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.composeUI.ErrorMessage
import com.numtory.application.composeUI.ItemNotFound
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.seke.domain.entities.SekePrice
import com.numtory.application.features.seke.presenter.components.CryptoListItem
import com.numtory.application.features.seke.presenter.components.SekeAppBar
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun SekePriceScreen(
    navigator: DestinationsNavigator,
    viewModel: SekePriceViewModel = koinViewModel()
) {

    val priceList by viewModel.priceState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            SekeAppBar()
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
                        val items = (priceList as ViewState.Success<List<SekePrice>>).data
                        if (items.isEmpty())
                            item {
                                ItemNotFound()
                            }
                        else
                            itemsIndexed(items) { index, itemState ->
                                CryptoListItem(itemState)
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

