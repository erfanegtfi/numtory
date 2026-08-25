package com.numtory.application.features.cryptoExchange.presenter.marketOptions

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges

@Composable
fun ExchangesListWithSelectAll(
    modifier: Modifier,
    allExchanges: List<ExchangeInfo>,
    checkedStates: MutableMap<Exchanges, Boolean>,
    onSelectedExchangesChanged: (List<Exchanges>) -> Unit
) {

    LazyColumn(
        modifier = modifier
    ) {
        items(allExchanges) { info ->
            ExchangeRow(
                info = info,
                isChecked = checkedStates[info.exchange] ?: false,
                onCheckedChange = { isChecked ->
                    checkedStates[info.exchange] = isChecked
                    val selected: List<Exchanges> =
                        allExchanges.filter { checkedStates[it.exchange] == true }
                            .map { it.exchange }
                    onSelectedExchangesChanged(selected)
                }
            )
            Divider()
        }
    }
}