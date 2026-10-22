package com.numtory.application.features.car.presenter.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numtory.application.features.car.domain.entities.Car
import com.numtory.application.features.car.domain.entities.Trend


@Composable
fun CategoryHeader(title: String?) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = title ?: "",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Divider(Modifier.padding(top = 6.dp))
    }
}


@Composable
fun CarRow(car: Car, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name (takes remaining space)
            Text(
                text = car.name ?: "",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.width(8.dp))

            // Price + change
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatPrice(car.price ?: 0L),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                ChangeChip(change = car.change, trend = car.trend)
            }
        }
    }
}

@Composable
private fun ChangeChip(change: Long?, trend: Trend?) {
//    if (trend == null) return   // nothing to show

    val isUp = trend == Trend.UP
    val color = if (isUp) Color(0xFF2E7D32) else Color(0xFFC62828)
    val icon = if (isUp) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val sign = if (isUp) "+" else ""

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$sign${formatPrice(change ?: 0L)}",
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


private fun formatPrice(value: Long): String =
    "%,d".format(value)   // 1,700

