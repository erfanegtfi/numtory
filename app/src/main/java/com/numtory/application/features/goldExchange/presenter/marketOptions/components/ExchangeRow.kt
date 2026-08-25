package com.numtory.application.features.goldExchange.presenter.marketOptions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo

@Composable
fun ExchangeRow(
    info: GoldExchangeInfo,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null, // Handled by row click
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        Box(
            modifier = Modifier.Companion.width(16.dp)
        )
        Image(
            painter = painterResource(id = info.exchange.logo),
            contentDescription = info.exchange.title,
            modifier = Modifier.Companion.size(26.dp)
        )
        Box(
            modifier = Modifier.Companion.width(16.dp)
        )
        Text(
            text = info.exchange.title,
        )
    }
}