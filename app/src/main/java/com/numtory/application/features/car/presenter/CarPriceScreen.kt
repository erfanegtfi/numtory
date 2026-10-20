package com.numtory.application.features.car.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.car.domain.entities.Car
import com.numtory.application.features.car.domain.entities.CarCategory
import com.numtory.application.features.car.domain.entities.Trend
import com.numtory.application.features.cryptoMarket.presenter.components.CryptoMarketAppBar
import com.numtory.application.features.cryptoMarket.presenter.components.CryptoMarketSearchAppBar
import com.numtory.application.features.seke.presenter.components.SekeAppBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CarPriceScreen(
    viewModel: CarPriceViewModel = koinViewModel(),
) {
    val state by viewModel.priceState.collectAsStateWithLifecycle()

    CarPriceContent(
        state = state,
        onRetry = viewModel::getPrices,
        modifier = Modifier,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun CarPriceContent(
    state: ViewState<List<CarCategory>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CarPriceViewModel,
) {
    var isSearching by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
                if (isSearching)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        CarSearchAppBar(onSearch = {
                            viewModel.filter(it)
                        }) { isSearching = false }
                    }
                else CarAppBar { isSearching = true }


        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = innerPadding.calculateTopPadding(), // Only top padding
                    start = innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Rtl)
                )
        ) {
            when (state) {
                is ViewState.Init,
                is ViewState.Loading -> LoadingView()

                is ViewState.Failure -> ErrorView(
                    message = state.error.message ?: "",
                    onRetry = onRetry
                )

                is ViewState.Success -> CarPriceList(state.data, viewModel)
            }

        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("تلاش مجدد")
        }
    }
}


@Composable
private fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun CarPriceList(categories: List<CarCategory>, viewModel: CarPriceViewModel) {
    val pullToRefreshState = rememberPullToRefreshState()
    val priceState by viewModel.priceState.collectAsStateWithLifecycle()


    PullToRefreshBox(
        isRefreshing = priceState is ViewState.Loading,
        onRefresh = { viewModel.getPrices() },
        state = pullToRefreshState,
        modifier = Modifier

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            categories.forEach { category ->
                // Category header (sticky per category)
                stickyHeader(key = "header_${category.name}") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        CategoryHeader(title = category.name)
                    }
                }

                // Cars inside this category
                if (category.cars != null)
                    items(
                        items = category.cars,
                        key = { "${it.name}" }
                    ) { car ->
                        CarRow(
                            car = car,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
            }
        }
    }
}

/* ---------- Category header ---------- */

@Composable
private fun CategoryHeader(title: String?) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = title ?: "",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Divider(Modifier.padding(top = 6.dp))
    }
}

/* ---------- Car row ---------- */

@Composable
private fun CarRow(car: Car, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name (takes remaining space)
            Text(
                text = car.name ?: "",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.width(8.dp))

            // Price + change
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatPrice(car.price ?: 0L),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                ChangeChip(change = car.change, trend = car.trend)
            }
        }
    }
}

@Composable
private fun ChangeChip(change: Long?, trend: Trend?) {
//    if (trend == null) return   // nothing to show

    val isUp = trend == Trend.UP
    val color = if (isUp) Color(0xFF2E7D32) else Color(0xFFC62828)
    val icon = if (isUp) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val sign = if (isUp) "+" else ""

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$sign${formatPrice(change ?: 0L)}",
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/* ---------- Helpers ---------- */

private fun formatPrice(value: Long): String =
    "%,d".format(value)   // 1,700

/* ---------- Loading ---------- */
