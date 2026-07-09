package com.numtory.application.features.market.presenter.components.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder
import com.numtory.application.ui.theme.Secondary

@Composable
fun MarketPriceHeader(sortField: SortField, sortOrder: SortOrder,  onSortChanged:(sortField: SortField, sortOrder: SortOrder) -> Unit) {
    var sortField by remember { mutableStateOf(sortField) }
    var sortOrder by remember { mutableStateOf(sortOrder) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Secondary)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.width(38.dp))
        Text("صرافی", modifier = Modifier.weight(1f), textAlign = TextAlign.Right)
        Box(modifier = Modifier.width(12.dp))

        SortableHeader(
            title = "فروش",
            field = SortField.SellPrice,
            currentField = sortField,
            currentOrder = sortOrder,
            onClick = {
                if (sortField == it) {
                    sortOrder =
                        if (sortOrder == SortOrder.Ascending) SortOrder.Descending else SortOrder.Ascending
                } else {
                    sortField = it
                    sortOrder = SortOrder.Ascending
                }
                onSortChanged(sortField, sortOrder)
//                sortParam.sortField = sortField
//                sortParam.sortOrder = sortOrder
//                onSortChanged(sortParam)
            },
            modifier = Modifier.weight(1f)
        )
        SortableHeader(
            title = "خرید",
            field = SortField.BuyPrice,
            currentField = sortField,
            currentOrder = sortOrder,
            onClick = {
                if (sortField == it) {
                    sortOrder =
                        if (sortOrder == SortOrder.Ascending) SortOrder.Descending else SortOrder.Ascending
                } else {
                    sortField = it
                    sortOrder = SortOrder.Ascending
                }
                onSortChanged(sortField, sortOrder)
//                sortParam.sortField = sortField
//                sortParam.sortOrder = sortOrder
//                onSortChanged(sortParam)
            },
            modifier = Modifier.weight(1f)
        )



        Text(
            "کارمزد",
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SortableHeader(
    title: String,
    field: SortField,
    currentField: SortField?,
    currentOrder: SortOrder,
    onClick: (SortField) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Row(
        modifier = modifier
            .clickable { onClick(field) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            textAlign = textAlign,
            style = MaterialTheme.typography.bodyLarge,
        )

        if (currentField == field) {
            Text(
                text = if (currentOrder == SortOrder.Ascending) " ↑" else " ↓",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
