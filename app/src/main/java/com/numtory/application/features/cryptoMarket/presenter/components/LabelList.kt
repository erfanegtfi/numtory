package com.numtory.application.features.cryptoMarket.presenter.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.numtory.application.features.cryptoMarket.domain.entities.LabelType
import com.numtory.application.features.scan.domain.entities.ScanNetwork

@Composable
fun SelectableLabelList(
    labels: List<LabelType>,
    selectedLabel: LabelType?,
    onLabelSelected: (LabelType?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(labels, key = { it }) { label ->
            val isSelected = label == selectedLabel
            SelectableLabelChip(
                text = label.display,
                selected = isSelected,
                onClick = {
                    // Tap selected item again to deselect (toggle)
                    onLabelSelected(if (isSelected) null else label)
                }
            )
        }
    }
}

@Composable
fun SelectableLabelChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceVariant

    val contentColor = if (selected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
//            indication = ScanNetwork.ripple(),
            onClick = onClick
        ),
        shape = RoundedCornerShape(16),
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = if (selected) 4.dp else 2.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}