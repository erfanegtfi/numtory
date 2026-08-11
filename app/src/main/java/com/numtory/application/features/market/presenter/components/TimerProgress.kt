package com.numtory.application.features.market.presenter.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.numtory.application.ui.theme.REFRESH_TIMER
import com.numtory.application.ui.theme.White

@Composable
fun TimerProgressBar(progress: Int) {
    LinearProgressIndicator(
        progress = { (progress / REFRESH_TIMER.toFloat()) },
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp),
        color = White,
        trackColor = MaterialTheme.colorScheme.primary,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}
