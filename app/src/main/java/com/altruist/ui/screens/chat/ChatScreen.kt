package com.altruist.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.navigation.NavController
import com.altruist.NavRoutes

@Composable
fun ChatScreen(
    relatedPost: Post,
    receiverUserId: Long,
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val messages by viewModel.messages.collectAsState()
    val currentMessage by viewModel.currentMessage.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val receiverUser by viewModel.receiverUser.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var isButtonVisible by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()
    val isOwnPost = currentUserId == relatedPost.user.id_user

    val shouldNavigateBack by viewModel.shouldNavigateBack.collectAsState()

    LaunchedEffect(shouldNavigateBack) {
        if (shouldNavigateBack) {
            navController.navigate(NavRoutes.MAINMENU)
            viewModel.clearNavigationFlag()
        }
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(receiverUserId) {
        viewModel.loadMessages(receiverUserId, relatedPost.id_post)
        viewModel.loadReceiverUser(receiverUserId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    AltruistScreenWrapper(
        statusBarColor = White,
        navigationBarColor = White
    ) {
        Scaffold(
            snackbarHost = {
                AltruistSnackbarHost(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            },
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
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
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    receiverUser?.let { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = user.profile_picture_url,
                                    contentDescription = "Imagen de usuario",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = user.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "@${user.username}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DarkGray
                                    )
                                }
                            }

                            if (isOwnPost && isButtonVisible) {
                                Button(
                                    onClick = { showDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFE59730),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Cerrar\ndonación",
                                        style = MaterialTheme.typography.labelMedium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = {
                                        Text(
                                            text = "¿Cerrar donación?",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "Esta acción eliminará el post y el chat asociado.",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            showDialog = false
                                            isButtonVisible = false
                                            currentUserId?.let {
                                                viewModel.closeDonation(receiverUserId, it, relatedPost.id_post)
                                            }
                                        }) {
                                            Text(
                                                text = "Sí",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showDialog = false }) {
                                            Text(
                                                text = "No",
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                            .background(Color(0xFFF9F3E9))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = relatedPost.imageUrls.firstOrNull(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = relatedPost.title,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(modifier = Modifier.padding(horizontal = 20.dp))
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .imePadding()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(messages) { message ->
                            MessageBubble(
                                message = message,
                                isOwnMessage = currentUserId == message.senderId
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = currentMessage,
                            onValueChange = { viewModel.onMessageChange(it) },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White)
                                .border(1.dp, Color.Black, MaterialTheme.shapes.medium),
                            placeholder = { Text("Escribe un mensaje") },
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                viewModel.sendMessage(
                                    receiverUserId,
                                    relatedPost.id_post
                                )
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_send),
                                contentDescription = "Enviar",
                                tint = Color.Black,
                                modifier = Modifier.fillMaxSize(0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}
