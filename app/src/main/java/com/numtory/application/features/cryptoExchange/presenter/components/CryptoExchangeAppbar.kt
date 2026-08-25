package com.numtory.application.features.cryptoExchange.presenter.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.numtory.application.R
import com.numtory.application.features.cryptoExchange.presenter.components.appbar.MarketTopBar
import com.numtory.application.features.cryptoExchange.presenter.components.appbar.TopBarAction
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CryptoMarketAppbar(
    selectedToken: State<String>,
    isDarkTheme: Boolean,
    onScannerClick: () -> Unit,
    toggleTheme: () -> Unit,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    return MarketTopBar(
        selectedToken = selectedToken,
        actions = {
            IconButton(onClick = onScannerClick) {
                Icon(
                    imageVector = Icons.Filled.ManageSearch,
                    contentDescription = "Network Scanner"
                )
            }

            IconButton(onClick = toggleTheme) {
                Icon(
                    imageVector = if (isDarkTheme)
                        Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = "Toggle theme"
                )
            }

            TopBarAction(
                icon = R.drawable.ic_about,
                contentDescription = "About",
                onClick = onAboutClick
            )

            TopBarAction(
                icon = R.drawable.ic_setting,
                contentDescription = "Settings",
                onClick = onSettingsClick
            )
        }
    )
}