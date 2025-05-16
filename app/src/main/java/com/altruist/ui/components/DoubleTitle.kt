package com.altruist.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.altruist.ui.theme.Gray
import com.altruist.ui.theme.TitleMediumTextStyle


@Composable
fun DoubleTitle(title1: String, title2: String) {
    Text(
        text = title1,
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = title2,
        style = MaterialTheme.typography.bodyLarge,
        color = Gray
    )
    Spacer(modifier = Modifier.height(12.dp))
}