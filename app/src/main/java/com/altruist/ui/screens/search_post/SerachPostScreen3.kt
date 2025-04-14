package com.altruist.ui.screens.search_post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.SearchPostViewModel
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.data.model.Post
import com.altruist.ui.components.DoubleTitle
import com.altruist.ui.components.PostItem
import com.altruist.ui.components.SearchBarAltruist
import com.altruist.ui.theme.YellowSearchScreen

@Composable
fun SearchPostScreen3(
    viewModel: SearchPostViewModel,
    onChangeCategoryClick: () -> Unit,
    onChangeLocationClick: () -> Unit,
    onChangeRangeClick: () -> Unit,
    onPostItemClick: (Post) -> Unit,
    onMainMenuClick: () -> Unit,
    onDonateClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val posts by viewModel.filteredPosts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
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
            floatingActionButton = {
                FloatingActionButton(onClick = { onMessagesClick() }) {
                    Icon(Icons.Default.Call, contentDescription = "Mensajes")
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    SearchBarAltruist (
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            //Esto de abajo hace falta?
                            //viewModel.onSearchQueryChange(it)
                        },
                        placeholder = "Busca una publicación",
                        modifier = Modifier
                            .fillMaxWidth(),
                        backgroundColor = YellowSearchScreen,
                        height = 56.dp,
                        borderWidth = 0.dp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Aquí añadirías los botones de filtros

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(posts) { post ->
                            PostItem(post = post)
                        }
                    }
                }
            }
        }
    }
}
