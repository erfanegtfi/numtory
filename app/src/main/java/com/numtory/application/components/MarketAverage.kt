package com.numtory.application.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.numtory.application.common.priceFormatter
import com.numtory.application.features.cryptoExchange.domain.entities.BestPrices
import com.numtory.application.ui.theme.BestPrice
import com.numtory.application.ui.theme.appColors

/** The buy and sell sides keep the same green/red they have in the average card above. */
private val BuyColor = BestPrice
private val SellColor = Color(0xFFEF4444)

/**
 * Four side-by-side stats over the list below: the best quote on each side — with the
 * exchange offering it — and the market average on each side.
 */
@Composable
fun MarketAverage(
    averageBuyPrice: Double,
    averageSellPrice: Double,
    bestPrices: BestPrices,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.appColors.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        StatCell(
            label = "بهترین خرید",
            value = formatPrice(bestPrices.buyPrice),
            caption = bestPrices.buyExchange,
            valueColor = BuyColor,
            modifier = Modifier.weight(1f)
        )
        StatCell(
            label = "بهترین فروش",
            value = formatPrice(bestPrices.sellPrice),
            caption = bestPrices.sellExchange,
            valueColor = SellColor,
            modifier = Modifier.weight(1f)
        )
        StatCell(
            label = "میانگین خرید",
            value = formatPrice(averageBuyPrice),
            valueColor = BuyColor,
            modifier = Modifier.weight(1f)
        )
        StatCell(
            label = "میانگین فروش",
            value = formatPrice(averageSellPrice),
            valueColor = SellColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
    valueColor: Color = Color.Unspecified,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
        // Only the best-price cells name an exchange; the averages have none to show.
        if (!caption.isNullOrBlank())
            Text(
                caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
    }
}

/** Prices arrive as doubles that are only ever shown whole; a missing one shows as a dash. */
private fun formatPrice(price: Double?): String =
    if (price == null || price.isNaN()) "-" else priceFormatter(price.toLong().toString())
