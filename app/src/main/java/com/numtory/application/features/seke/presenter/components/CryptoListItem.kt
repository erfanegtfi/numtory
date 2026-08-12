package com.numtory.application.features.seke.presenter.components;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numtory.application.BuildConfig
import com.numtory.application.common.formatDuration
import com.numtory.application.common.priceFormatter
import com.numtory.application.composeUI.MyImageLoader
import com.numtory.application.features.seke.domain.entities.SekePrice
import java.util.Locale

@Composable
fun CryptoListItem(seke: SekePrice) {

    val totalSeconds =
            (System.currentTimeMillis() - (seke.lastUpdateSec
                    ?: System.currentTimeMillis())) / 1000


    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,


            ) {
        // Left section: Image, Name & Symbol
        Row(

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),

                ) {

            MyImageLoader(
                    BuildConfig.SEKE_ICON_URL.replace(
                            "{icon}",
                            seke.symbol?.uppercase() ?: ""
                )
            )

            // Name and Symbol
            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = (seke.title
                                ?: "").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )
                Text(
                        text = "${formatDuration(totalSeconds)} قبل",
                        fontSize = 12.sp,
                        color = Color.Gray
                )
            }

            Text(
                    text = priceFormatter(seke.sell ?: "0"),
            fontSize = 14.sp,
            )
        }
    }

}
