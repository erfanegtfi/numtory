package com.numtory.application.features.goldExchange.presenter.marketOptions.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges

@Composable
fun ExchangesListWithSelectAll(
    modifier: Modifier,
    allExchanges: List<GoldExchangeInfo>,
    checkedStates: MutableMap<GoldExchanges, Boolean>,
    onSelectedExchangesChanged: (List<GoldExchanges>) -> Unit
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
                    val selected: List<GoldExchanges> =
                        allExchanges.filter { checkedStates[it.exchange] == true }
                            .map { it.exchange }
                    onSelectedExchangesChanged(selected)
                }
            )
            Divider()
        }
    }
}