package com.numtory.application.composeUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyScaffold(
    topBar: @Composable (() -> Unit)? = null,
    bottomBarContent: @Composable (() -> Unit)? = null,
    floatingActionButtonContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor  = MaterialTheme.colorScheme.background,
        topBar = {
            topBar?.invoke()
        },
        bottomBar = {
            bottomBarContent?.invoke()
        },
        floatingActionButton = {
            floatingActionButtonContent?.invoke()
        },
        content = { paddingValues ->
            // Custom body content with padding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content()
            }
        }
    )
}