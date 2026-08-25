package com.numtory.application.features.cryptoMarket.presenter.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoMarketSearchAppBar(
    onSearch: (String) -> Unit,
    onClose: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }

    // Search field filling the whole app bar
    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it)
        },
        modifier = Modifier.Companion
            .fillMaxWidth()
            .height(59.dp), // Match TopAppBar height
        placeholder = { Text("جستجو...") },
        leadingIcon = {
            // Search icon at the start
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.Companion.size(24.dp)
            )
        },
        trailingIcon = {
            // Close/X icon to exit search
            IconButton(
                onClick = {
                    onClose()
                    searchText = ""
                    onSearch("")
                }
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close search"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            focusedIndicatorColor = Color.Companion.Transparent,
            unfocusedIndicatorColor = Color.Companion.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        shape = RectangleShape // Remove rounded corners
    )


}