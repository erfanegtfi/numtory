package com.numtory.application.features.market.presenter.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.numtory.application.BuildConfig
import com.numtory.application.R
import com.numtory.application.common.priceFormatter
import com.numtory.application.composeUI.MyImageLoader

@Composable
fun GetMarketAverage(
    title: String,
    subtitle: String,
    symbol: String,
    onTokenClicked: () -> Unit,
    onChartClicked: () -> Unit,
) {

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.chart),
            contentDescription = title,
            modifier = Modifier.clickable {
                onChartClicked()
            }
        )

        MyImageLoader(
            BuildConfig.CRYPTO_ICON_URL.replace(
                "{icon}",
                symbol.lowercase()
            ), modifier = Modifier.clickable() {
                onTokenClicked()
            }
        )

        Column(
            Modifier
                .weight(1f)
                .clickable { onTokenClicked() },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Buy Price
//            PriceChip(
//                label = "میانگین خرید",
//                value = priceFormatter(averageBuyPrice.toLong().toString()),
//                color = Color(0xFF10B981) // Green
//            )
//
//            // Sell Price
//            PriceChip(
//                label = "میانگین فروش",
//                value = priceFormatter(averageSellPrice.toLong().toString()),
//                color = Color(0xFFEF4444) // Red
//            )
        }
    }
}

@Composable
private fun PriceChip(label: String, value: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}