package com.altruist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.altruist.data.model.Category
import com.altruist.ui.theme.DarkYellow
import com.altruist.ui.theme.YellowLight2

@Composable
fun CategoryButton(
    category: Category,
    onClick: () -> Unit,
    isMainCategory: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = if (isMainCategory) DarkYellow else YellowLight2,
            modifier = Modifier
                .size(80.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = category.icon_url,
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

        Text(
            text = category.name,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
