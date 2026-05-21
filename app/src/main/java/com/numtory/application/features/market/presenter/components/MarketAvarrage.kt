package com.numtory.application.features.market.presenter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numtory.application.R
import com.numtory.application.common.priceFormatter
import com.numtory.application.ui.theme.White

@Composable
fun GetMarketAverage(averageBuyPrice: Float, averageSellPrice: Float) {

    return Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
//        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Image(
            painter = painterResource(id = R.drawable.tether),
            contentDescription = "",
            modifier = Modifier
                .size(26.dp),
        )
        Box( modifier = Modifier.width(16.dp))
        Column (
            modifier = Modifier .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "طلا",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "GOLD",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "میانگین   خرید:  ${priceFormatter((averageBuyPrice).toInt().toString())}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
            )
            Text(
                text = "میانگین فروش:  ${priceFormatter((averageSellPrice).toInt().toString())}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
            )
        }
    }

}