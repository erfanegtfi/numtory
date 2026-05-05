package com.numtory.application.features.market.presenter.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.numtory.application.common.formatDuration
import com.numtory.application.common.priceFormatter
import com.numtory.application.features.market.domain.entities.MarketPrice


@SuppressLint("DefaultLocale")
@Composable
fun CryptoPriceItem(item: MarketPrice, modifier: Modifier = Modifier) {
//    print(System.currentTimeMillis().minus(item.lastRefresh!!))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = item.exchange?.logo ?: 0),
            contentDescription = "Description of image",
            modifier = Modifier
                .padding(8.dp)
                .size(26.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.weight(1.2f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "${item.exchange?.title}",
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
            priceFormatter((item.finalSellPrice.toFloat().toInt().toString())),
            modifier = Modifier.weight(0.9f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )


        Text(
            priceFormatter((item.finalBuyPrice.toFloat().toInt().toString())),
            modifier = Modifier.weight(0.9f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )

        Column(
            modifier = Modifier.width(55.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "${
                    if ((item.exchange?.fee ?: 0f) == 0f) 0 else String.format(
                        "%.2f",
                        (item.exchange?.fee ?: 0f) * 100
                    )
                }%",
                style = MaterialTheme.typography.bodyMedium
            )

            if (item.addFee == true)
                Text(
                    priceFormatter(item.diff),
                    style = MaterialTheme.typography.bodyMedium
                )
        }
    }
}
