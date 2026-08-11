package com.numtory.application.features.seke.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.BuildConfig
import com.numtory.application.common.formatDuration
import com.numtory.application.common.priceFormatter
import com.numtory.application.composeUI.ErrorMessage
import com.numtory.application.composeUI.ItemNotFound
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.market.presenter.components.TimerProgressBar
import com.numtory.application.features.seke.domain.entities.SekePrice
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar() {
    return TopAppBar(
        title = {

            // Normal title
            Text(
                text = "قیمت ارز و سکه",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

        },
        navigationIcon = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {

        }
    )
}

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
            MyTopAppBar()
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
                        val items = (priceList as ViewState.Success<List<SekePrice>>).data
                        if (items.isEmpty())
                            item {
                                ItemNotFound()
                            }
                        else
                            itemsIndexed(items) { index, itemState ->
                                CryptoListItem(
                                    itemState
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

@Composable
fun CryptoListItem(seke: SekePrice) {

    val totalSeconds =
        (System.currentTimeMillis() - (seke.lastUpdateSec
            ?: System.currentTimeMillis())) / 1000


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

            MyImageLoader(
                BuildConfig.SEKE_ICON_URL.replace(
                    "{icon}",
                    seke.symbol?.uppercase() ?: ""
                )
            )

            // Name and Symbol
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = (seke.title
                        ?: "").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${formatDuration(totalSeconds)} قبل",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = priceFormatter(seke.sell ?: "0"),
                fontSize = 14.sp,
            )
        }
    }

}