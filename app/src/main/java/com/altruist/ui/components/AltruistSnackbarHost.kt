package com.altruist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
@Composable
fun AltruistSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            val messageRaw = snackbarData.visuals.message
            val isSuccess = messageRaw.startsWith("SUCCESS|")
            val message = messageRaw.removePrefix("SUCCESS|")

            val backgroundColor = if (isSuccess) Color(0xFFE0FFE0) else Color(0xFFFFE0E0)
            val borderColor = if (isSuccess) Color(0xFF4CAF50) else Color.Red
            val textColor = if (isSuccess) Color(0xFF2E7D32) else Color.Red

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = backgroundColor,
                border = BorderStroke(1.dp, borderColor),
                shadowElevation = 4.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(onClick = { snackbarData.dismiss() }) {
                        Text("Cerrar", color = Color.DarkGray)
                    }
                }
            }
        }
    )
}
