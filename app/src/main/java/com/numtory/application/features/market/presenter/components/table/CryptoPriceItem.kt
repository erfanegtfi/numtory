package com.numtory.application.features.market.presenter.components.table

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.numtory.application.common.formatDuration
import com.numtory.application.common.priceFormatter
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.ui.theme.BestPrice
import kotlin.text.toLong


@SuppressLint("DefaultLocale")
@Composable
fun CryptoPriceItem(
    item: MarketPrice,
    isBestSell: Boolean = false,
    isBestBuy: Boolean = false,
    modifier: Modifier = Modifier
) {
//    print(System.currentTimeMillis().minus(item.lastRefresh!!))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 1.dp, end = 2.dp, top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = item.exchangeInfo.exchange.logo),
            contentDescription = "Description of image",
            modifier = Modifier
                .padding(8.dp)
                .size(26.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                item.exchangeInfo.exchange.title,
                style = MaterialTheme.typography.bodyMedium
            )

            val totalSeconds =
                (System.currentTimeMillis() - (item.lastRefresh
                    ?: System.currentTimeMillis())) / 1000

            Text(
                text = formatDuration(
                    totalSeconds
                ),
                style = MaterialTheme.typography.labelMedium,
            )
        }

        Text(
            priceFormatter((item.finalSellPrice.toDouble().toLong().toString())).take(12),
            modifier = Modifier
                .weight(1f)
                .then(
                    if (isBestSell)
                        Modifier.background(BestPrice.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    else Modifier
                )
                .padding(vertical = 4.dp),
            overflow = TextOverflow.Clip,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = if (isBestSell) BestPrice else Color.Unspecified,
            fontWeight = if (isBestSell) FontWeight.Bold else null,
            style = MaterialTheme.typography.bodyMedium
        )


        Text(
            priceFormatter((item.finalBuyPrice.toDouble().toLong().toString())).take(12),
            modifier = Modifier
                .weight(1f)
                .then(
                    if (isBestBuy)
                        Modifier.background(BestPrice.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    else Modifier
                )
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            maxLines = 1,
            color = if (isBestBuy) BestPrice else Color.Unspecified,
            fontWeight = if (isBestBuy) FontWeight.Bold else null,
            style = MaterialTheme.typography.bodyMedium
        )

        Column(
            modifier = Modifier.width(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${
                    if ((item.exchangeInfo.fee ?: 0f) == 0f) 0 else String.format(
                        "%.2f",
                        (item.exchangeInfo.fee ?: 0f) * 100
                    )
                }%",
                style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
            )

            if (item.addFee == true)
                Text(
                    priceFormatter(item.diff).take(8),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
        }
    }
}
