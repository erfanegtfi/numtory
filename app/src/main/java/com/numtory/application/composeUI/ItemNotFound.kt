package com.numtory.application.composeUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemNotFound(title: String = "موردی پیدا نشد") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(300.dp)
    ) {
        Text(
            title,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}