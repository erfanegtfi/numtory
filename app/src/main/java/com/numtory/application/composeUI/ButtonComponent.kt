package com.eterex.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonComponent(value: String, modifier: Modifier = Modifier, onButtonClicked: () -> Unit) {
    Button(
        modifier = modifier.fillMaxWidth().heightIn(45.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
//        colors = ButtonDefaults.buttonColors(Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary,

            ),
        shape = RoundedCornerShape(50.dp),
    ) {

        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun ActionButtonPreview_NavigationVisible() {
    ButtonComponent("sdfsdf") { }
}