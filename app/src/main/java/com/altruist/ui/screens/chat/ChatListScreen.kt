package com.altruist.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.altruist.R
import com.altruist.data.model.Post
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.MessageBubble
import com.altruist.ui.theme.DarkGray
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.ChatViewModel
import androidx.compose.foundation.layout.imePadding
import com.altruist.ui.theme.Gray
import com.altruist.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen(
    navToChat: (Post, Long) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val chatPreviews by viewModel.chatPreviews.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    AltruistScreenWrapper(statusBarColor = White, navigationBarColor = White) {
        Scaffold(snackbarHost = {
            AltruistSnackbarHost(snackbarHostState)
        }) { innerPadding ->

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
                        text = "Mis mensajes",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f), // ocupa el resto de la pantalla bajo el título
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isLoading -> {
                                CircularProgressIndicator()
                            }

                            //TODO: si no hay mensajes mostrar un mensaje
                            chatPreviews.isEmpty() -> {
                                Text(
                                    text = "Cargando tus mensajes...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Gray,
                                    textAlign = TextAlign.Center
                                )
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(chatPreviews) { preview ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    navToChat(
                                                        preview.relatedPost,
                                                        preview.otherUser.id_user
                                                    )
                                                }
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AsyncImage(
                                                model = preview.otherUser.profile_picture_url,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = preview.otherUser.name,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    text = preview.lastMessage.content,
                                                    maxLines = 1,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                            Text(
                                                text = viewModel.formatTimestamp(preview.lastMessage.timestamp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = DarkGray
                                            )
                                        }
                                        Divider(color = Color(0xFFE59730), thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

