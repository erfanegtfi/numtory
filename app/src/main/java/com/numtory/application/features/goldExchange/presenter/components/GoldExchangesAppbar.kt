package com.numtory.application.features.goldExchange.presenter.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.numtory.application.R
import com.numtory.application.features.cryptoExchange.presenter.components.appbar.MarketTopBar
import com.numtory.application.features.cryptoExchange.presenter.components.appbar.TopBarAction
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun GoldExchangesAppbar(
    selectedToken: State<String>,
    onSettingsClick: () -> Unit
) {
    return MarketTopBar(
        selectedToken = selectedToken,
        actions = {
            TopBarAction(
                icon = R.drawable.ic_setting,
                contentDescription = "Settings",
                onClick = onSettingsClick
            )
        }
    )

}