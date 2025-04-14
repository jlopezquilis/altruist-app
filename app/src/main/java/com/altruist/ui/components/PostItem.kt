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
import com.altruist.data.model.Post
import com.altruist.ui.theme.Gray
import com.altruist.ui.theme.Shapes
import com.altruist.ui.theme.YellowSearchScreen


@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, Shapes.medium)
            .background(YellowSearchScreen)
            .padding(16.dp)
            //.clickable()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(Shapes.medium)
            ) {
                AsyncImage(
                    //model = post.images.firstOrNull(),
                    model = "https://firebasestorage.googleapis.com/v0/b/altruist-app-73b03.firebasestorage.app/o/profile_pictures%2Fd2c7d708-a0bf-449c-b876-855f34c6ce17.jpg?alt=media&token=4b60b3f6-c6e6-42a2-a0f7-49e3346a71df",
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Distancia abajo a la derecha
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(Shapes.large)
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location_marker_gray),
                        contentDescription = "Ubicación",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "15 km",
                        color = Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                AsyncImage(
                    model = "https://firebasestorage.googleapis.com/v0/b/altruist-app-73b03.firebasestorage.app/o/profile_pictures%2F8827fefb-7c98-423a-bad1-8a577c841616.jpg?alt=media&token=2f77180a-d4aa-4052-ab01-5e66eaf1c693", // URL real del avatar del user
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Pedro", // Nombre del usuario
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
    }
}
