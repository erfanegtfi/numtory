package com.numtory.application.features.cryptoExchange.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numtory.application.BuildConfig
import com.numtory.application.common.exchangesTokenListScreenOpened
import com.numtory.application.composeUI.BottomSheetAppbar
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenListBottomSheetScreen(
    tokenList: List<String>,
    hide: () -> Unit,
    onSubmit: (token: String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomSheetAppbar(hide, title = "رمزارزها")
        Box(modifier = Modifier.height(16.dp))


        ExchangesListWithSelectAll(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            tokenList,
        ) { token ->
            exchangesTokenListScreenOpened(token)
            onSubmit(token)
        }
        Box(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun ExchangesListWithSelectAll(
    modifier: Modifier,
    tokenList: List<String>,
    onTokenSelected: (String) -> Unit
) {

    LazyColumn(
        modifier = modifier
    ) {
        items(tokenList) { symbol ->
            ExchangeRow(
                symbol = symbol,
                onCheckedChange = { selectedSymbol ->
                    onTokenSelected(selectedSymbol)
                }
            )
        }
    }
}

@Composable
fun ExchangeRow(
    symbol: String,
    onCheckedChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(symbol) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        MyImageLoader(
            BuildConfig.CRYPTO_ICON_URL.replace(
                "{icon}",
                symbol.lowercase()
            )
        )
        Box(
            modifier = Modifier.width(16.dp)
        )
        Text(
            text = symbol,
        )
    }
}

@Preview
@Composable
fun AssetOptionsBottomSheetScreenPreview() {
    MyApplicationTheme {
//        AssetOptionsBottomSheetScreen(emptyList(), false, hide = {}, onSubmit = { a, b -> })
    }
}