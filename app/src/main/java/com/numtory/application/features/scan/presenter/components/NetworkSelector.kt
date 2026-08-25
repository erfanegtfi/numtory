package com.numtory.application.features.scan.presenter.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.numtory.application.BuildConfig
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.scan.domain.entities.ScanNetwork

@Composable
fun NetworkSelector(
    network: ScanNetwork,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MyImageLoader(
            BuildConfig.CRYPTO_ICON_URL.replace("{icon}", network.symbol.lowercase())
        )

        Text(
            text = network.title,
            modifier = Modifier.Companion.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Companion.Bold,
        )

        Text(
            text = network.symbol,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Icon(Icons.Filled.ArrowDropDown, contentDescription = "انتخاب شبکه")
    }
}