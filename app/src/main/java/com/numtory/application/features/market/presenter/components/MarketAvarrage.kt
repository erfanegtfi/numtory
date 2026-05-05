package com.numtory.application.features.market.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.numtory.application.common.priceFormatter

@Composable
fun GetMarketAverage(averageBuyPrice: Float, averageSellPrice: Float) {

    return Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "میانگین فروش ${priceFormatter((averageSellPrice).toInt().toString())}",
        )

        Text(
            text = "میانگین خرید ${priceFormatter((averageBuyPrice).toInt().toString())}",
        )
    }

}