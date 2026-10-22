package com.numtory.application.features.car.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.car.domain.entities.CarCategory
import com.numtory.application.features.car.presenter.components.CarRow
import com.numtory.application.features.car.presenter.components.CategoryHeader
import com.numtory.application.features.car.presenter.components.ErrorView
import com.numtory.application.features.car.presenter.components.LoadingView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CarPriceScreen(
    navigator: DestinationsNavigator,
    viewModel: CarPriceViewModel = koinViewModel(),
) {
    val state by viewModel.priceState.collectAsStateWithLifecycle()

    CarPriceContent(
        navigator = navigator,
        state = state,
        onRetry = viewModel::getPrices,
        modifier = Modifier,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun CarPriceContent(
    navigator: DestinationsNavigator,
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
            else CarAppBar(onSearch = {
                isSearching = true
            }, onBackClick = {
                navigator.popBackStack()
            })


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
            item {
                Text("این قیمت ها صرفا اعلامی فروشندگان است و لزوما قیمت معامله نیست",
                modifier = Modifier.padding(6.dp))
            }
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