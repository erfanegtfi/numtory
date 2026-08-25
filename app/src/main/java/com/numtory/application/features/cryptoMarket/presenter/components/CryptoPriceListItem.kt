package com.numtory.application.features.cryptoMarket.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numtory.application.common.priceFormatter
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import java.util.Locale
import kotlin.math.abs

@Composable
fun CryptoPriceListItem(crypto: CryptoMarketPrice) {

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically,


        ) {
        // Left section: Image, Name & Symbol
        Row(
            modifier = Modifier.Companion.weight(1f),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),

            ) {
            // Crypto Icon/Image
//            MyImageLoader(
//                BuildConfig.CRYPTO_ICON_URL.replace(
//                    "{icon}",
//                    crypto.symbol?.lowercase() ?: ""
//                )
//            )
            MyImageLoader(
                crypto.image ?: ""
            )

            // Name and Symbol
            Column() {
                Text(
                    text = (crypto.name
                        ?: "").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Companion.Ellipsis
                )
                Text(
                    text = crypto.symbol?.uppercase() ?: "",
                    fontSize = 12.sp,
                    color = Color.Companion.Gray
                )
            }
        }

        // Right section: Price and Change
        Column(horizontalAlignment = Alignment.Companion.End) {
            Text(
                text = priceFormatter(crypto.price ?: "0"),
                fontSize = 14.sp,
            )

            // Price Change with color indicator
            val changeColor = if (crypto.dayChangePercent >= 0)
                Color(0xFF4CAF50) else Color(0xFFF44336)
            val changeSymbol = if (crypto.dayChangePercent >= 0) "▲" else "▼"

            Text(
                text = "${
                    String.format("%.2f", abs(crypto.dayChangePercent))
                }% $changeSymbol  $${
                    String.format("%.2f", abs(crypto.dayChangePrice))
                }",
                fontSize = 13.sp,
                fontWeight = FontWeight.Companion.Medium,
                color = changeColor
            )
        }
    }
}