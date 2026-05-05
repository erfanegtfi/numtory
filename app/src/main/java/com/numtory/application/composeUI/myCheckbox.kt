package com.numtory.application.composeUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyCheckbox(
    title: String,
    checked: Boolean,
    innerPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
    onCheckedChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(checked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                checked = !checked
                onCheckedChange(checked)  // Call the callback
            }
            .padding(innerPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null // Disable individual click, handled by row
        )
        Box(modifier = Modifier.width(12.dp))
        Text(
            text = title,
        )

    }
}