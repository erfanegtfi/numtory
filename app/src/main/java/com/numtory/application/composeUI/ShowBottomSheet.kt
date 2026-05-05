package com.numtory.application.composeUI

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable (sheetState: SheetState, hide: () -> Unit) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val coroutineScope = rememberCoroutineScope()
    val hideModalBottomSheet: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        dragHandle = null, // { BottomSheetDefaults.DragHandle() },
    ) {
        content(sheetState, hideModalBottomSheet)
    }
}