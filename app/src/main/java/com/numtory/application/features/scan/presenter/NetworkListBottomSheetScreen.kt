package com.numtory.application.features.scan.presenter

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.numtory.application.BuildConfig
import com.numtory.application.composeUI.BottomSheetAppbar
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.scan.domain.entities.ScanNetwork

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkListBottomSheetScreen(
    networks: List<ScanNetwork>,
    selected: ScanNetwork,
    hide: () -> Unit,
    onSubmit: (network: ScanNetwork) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomSheetAppbar(hide, title = "شبکه‌ها")
        Box(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(networks) { network ->
                NetworkRow(
                    network = network,
                    isSelected = network == selected,
                    onClick = { onSubmit(network) }
                )
            }
        }

        Box(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun NetworkRow(
    network: ScanNetwork,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyImageLoader(
            BuildConfig.CRYPTO_ICON_URL.replace("{icon}", network.symbol.lowercase())
        )

        Box(modifier = Modifier.width(16.dp))

        Text(
            text = network.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else null,
        )

        Text(
            text = network.symbol,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
