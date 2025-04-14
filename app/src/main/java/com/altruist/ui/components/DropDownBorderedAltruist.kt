package com.altruist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.altruist.ui.theme.Shapes
import com.altruist.ui.theme.White
import com.altruist.ui.theme.DarkYellow


@Composable
fun DropDownBorderedAltruist(
    itemSelected: String,
    items: List<String>, // Asegúrate de que Category tenga una propiedad `name`
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    placeholderText: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(White, shape = Shapes.medium)
            .border(1.dp, DarkYellow, shape = Shapes.medium)
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onExpandedChange(true) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (itemSelected.isBlank()) placeholderText else itemSelected,
                style = MaterialTheme.typography.bodyLarge,
                color = if (itemSelected.isBlank()) Color.Gray.copy(alpha = 0.5f) else Color.Black
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            items.forEach { itemSelected ->
                DropdownMenuItem(
                    text = { Text(itemSelected) },
                    onClick = {
                        onItemSelected(itemSelected)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
