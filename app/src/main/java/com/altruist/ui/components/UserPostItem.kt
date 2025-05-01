package com.altruist.ui.components

import com.altruist.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.*
import com.altruist.data.model.Post
import com.altruist.ui.theme.LightGray
import com.altruist.ui.theme.Shapes
import com.altruist.ui.theme.YellowSearchScreen
import com.altruist.utils.dto.UserPostUI

@Composable
fun UserPostItem(
    userPostUI: UserPostUI,
    onPostItemClick: (Post) -> Unit,
    onDeleteClick: () -> Unit,
    onViewInterestedClick: () -> Unit
) {
    val post = userPostUI.post
    val interestedCount = userPostUI.requests.size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onPostItemClick(post)}
            .shadow(elevation = 8.dp, shape = Shapes.medium)
            .background(YellowSearchScreen)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Primera fila: mitad imagen - mitad botón eliminar centrado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // altura fija para alinear bien imagen y botón
            ) {
                // Parte izquierda: Imagen
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(Shapes.medium)
                ) {
                    AsyncImage(
                        model = post.imageUrls.firstOrNull(),
                        contentDescription = "Imagen del post",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                // Parte derecha: botón eliminar centrado
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .size(48.dp)
                            .background(LightGray, shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Eliminar",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Segunda fila: título, interesados, botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$interestedCount interesad${if (interestedCount == 1) "o" else "os"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                SmallSecondaryButton(
                    text = "Ver interesados",
                    onClick = onViewInterestedClick,
                    enabled = true,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}