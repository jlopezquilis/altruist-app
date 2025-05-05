package com.altruist.ui.screens.user_posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import com.altruist.data.model.Post
import com.altruist.data.model.User
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.SmallSecondaryButton
import com.altruist.ui.theme.Shapes
import com.altruist.ui.theme.White
import com.altruist.ui.theme.YellowSearchScreen
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.utils.dto.UserPostUI
import com.altruist.viewmodel.UserPostsViewModel


@Composable
fun UserPostApplicantsScreen(
    viewModel: UserPostsViewModel,
    onPostItemClick: (Post) -> Unit,
    userPost: UserPostUI,
    onOpenChatClick: (User, Post) -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    AltruistScreenWrapper(
        statusBarColor = White,
        navigationBarColor = White
    ) {
        Scaffold(
            snackbarHost = {
                AltruistSnackbarHost(snackbarHostState)
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "Interesados en:",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {onPostItemClick(userPost.post)}
                            .shadow(elevation = 8.dp, shape = Shapes.medium)
                            .background(YellowSearchScreen)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = userPost.post.imageUrls.firstOrNull(),
                                contentDescription = "Imagen del post",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(MaterialTheme.shapes.medium)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = userPost.post.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Divider(color = Color(0xFFE59730), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (userPost.requests.isEmpty()) {
                        Text(
                            text = "Aún no hay interesados",
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 10.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(userPost.requests) { request ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = request.user.profile_picture_url,
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = request.user.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${request.user.id_user}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    SmallSecondaryButton(
                                        onClick = { onOpenChatClick(request.user, userPost.post) },
                                        text = "Abrir chat",
                                        enabled = true,
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = Color(0xFFE59730), thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}
