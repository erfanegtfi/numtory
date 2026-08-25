package com.numtory.application.features.cryptoExchange.presenter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.numtory.application.components.MarketAverage
import com.numtory.application.components.SelectedMarketToken
import com.numtory.application.components.TimerProgressBar
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.cryptoMap
import com.numtory.application.features.cryptoExchange.domain.entities.BestPrices
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CryptoExchangeStickyHeader(
    priceList: ViewState<List<MarketPrice>>,
    timer: State<Int>,
    averageBuyPrice: Double, averageSellPrice: Double,
    selectedToken: State<String>,
    bestPrices: BestPrices,
    onTokenClick: () -> Unit,
    onChartClicked: () -> Unit,
) {
    return Column {
        Box(modifier = Modifier.Companion.height(2.dp))
        TimerProgressBar(timer)
        if (priceList is ViewState.Success<List<MarketPrice>>) {


            SelectedMarketToken(
                cryptoMap[selectedToken.value] ?: "",
                selectedToken,
                selectedToken,
                onChartClicked = onChartClicked,
                onTokenClicked = onTokenClick

            )

            MarketAverage(
                averageBuyPrice = averageBuyPrice,
                averageSellPrice = averageSellPrice,
                bestPrices = bestPrices,
            )
        }
    }
}