package com.altruist.ui.screens.user_posts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.data.model.Post
import com.altruist.utils.dto.UserPostUI
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.SearchPostViewModel
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.SearchBarAltruist
import com.altruist.ui.components.UserPostItem
import com.altruist.ui.theme.YellowSearchScreen
import com.altruist.viewmodel.SharedViewModel
import com.altruist.viewmodel.UserPostsViewModel
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch


@Composable
fun UserPostsScreen(
    viewModel: UserPostsViewModel = hiltViewModel(),
    onViewInterestedClick: (post: Post) -> Unit,
    onViewPostClick: (post: Post) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val posts by viewModel.filteredUserPosts.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val coroutineScope = rememberCoroutineScope()

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
                        text = "Mis donaciones",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SearchBarAltruist(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = "Buscar por título",
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = YellowSearchScreen,
                        height = 56.dp,
                        borderWidth = 0.dp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (posts.isEmpty()) {
                        Text(
                            text = "No has publicado ninguna donación.",
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 80.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 40.dp)
                        ) {

                            items(posts, key = { it.post.id_post }) { userPostUI ->
                                var visible by remember { mutableStateOf(true) }
                                var showDialog by remember { mutableStateOf(false) }

                                AnimatedVisibility(
                                    visible = visible,
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    UserPostItem(
                                        userPostUI = userPostUI,
                                        onDeleteClick = {
                                            showDialog = true
                                        },
                                        onViewInterestedClick = {
                                            onViewInterestedClick(userPostUI.post)
                                        },
                                        onPostItemClick = {
                                            onViewPostClick(userPostUI.post)
                                        }
                                    )
                                }

                                if (showDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDialog = false },
                                        title = { Text(
                                            text = "¿Eliminar publicación?",
                                            style = MaterialTheme.typography.titleMedium
                                        ) },
                                        text = { Text(
                                            text = "Esta acción no se puede deshacer.",
                                            style = MaterialTheme.typography.bodyMedium
                                        ) },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                showDialog = false
                                                visible = false
                                                coroutineScope.launch {
                                                    kotlinx.coroutines.delay(300)
                                                    viewModel.deletePostFromList(userPostUI.post.id_post)
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
                    }
                }
            }
        }
    }
}
