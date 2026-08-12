package com.numtory.application.features.cryptoMarket.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import com.numtory.application.common.priceFormatter
import com.numtory.application.composeUI.ItemNotFound
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.market.presenter.components.TimerProgressBar
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoAppBar(
    onSearch: (String) -> Unit,
    onClose: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }

    // Search field filling the whole app bar
    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(59.dp), // Match TopAppBar height
        placeholder = { Text("جستجو...") },
        leadingIcon = {
            // Search icon at the start
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            // Close/X icon to exit search
            IconButton(
                onClick = {
                    onClose()
                    searchText = ""
                    onSearch("")
                }
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close search"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        shape = RectangleShape // Remove rounded corners
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onSearch: () -> Unit) {
    return TopAppBar(
        title = {

            // Normal title
            Text(
                text = "قیمت رمز ارزها",
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
            IconButton(onClick = { onSearch() }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun CryptoListScreen(
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
                    CryptoAppBar(onSearch = {
                        viewModel.filter(it)
                    }) { isSearching = false }
                }
            else MyTopAppBar { isSearching = true }

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
                        TimerProgressBar(viewModel.timer.value)
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
                        val items = (priceList as ViewState.Success<List<CryptoMarketPrice>>).data
                        if (items.isEmpty())
                            item {
                                ItemNotFound()
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
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),

            ) {
            // Crypto Icon/Image
//            MyImageLoader(
//                BuildConfig.CRYPTO_ICON_URL.replace(
//                    "{icon}",
//                    crypto.symbol?.lowercase() ?: ""
//                )
//            )
            MyImageLoader(
                crypto.image ?: ""
            )

            // Name and Symbol
            Column() {
                Text(
                    text = (crypto.name
                        ?: "").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                text = priceFormatter(crypto.price?:"0"),
                fontSize = 14.sp,
            )

            // Price Change with color indicator
            val changeColor = if (crypto.dayChangePercent >= 0)
                Color(0xFF4CAF50) else Color(0xFFF44336)
            val changeSymbol = if (crypto.dayChangePercent >= 0) "▲" else "▼"

            Text(
                text = "${
                    String.format(
                        "%.2f",
                        kotlin.math.abs(crypto.dayChangePercent)
                    )
                }% $changeSymbol  $${
                    String.format(
                        "%.2f",
                        kotlin.math.abs(crypto.dayChangePrice)
                    )
                }",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = changeColor
            )
        }
    }
}