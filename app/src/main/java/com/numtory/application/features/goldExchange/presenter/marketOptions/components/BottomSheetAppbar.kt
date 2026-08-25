package com.numtory.application.features.goldExchange.presenter.marketOptions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numtory.application.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAppbar(hide: () -> Unit, title: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        IconButton(
            onClick = {
                hide()
            }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_close),
                tint = Color.Companion.Black,
                modifier = Modifier.Companion
                    .padding(start = 16.dp)
                    .size(18.dp),
                contentDescription = R.drawable.ic_close.toString()
            )
        }

        Text(
            title,
            modifier = Modifier.Companion.padding(end = 16.dp),
            fontSize = 16.sp
        )
    }
}