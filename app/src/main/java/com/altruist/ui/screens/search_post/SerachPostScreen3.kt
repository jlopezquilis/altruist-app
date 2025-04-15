package com.altruist.ui.screens.search_post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.altruist.R
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.SearchPostViewModel
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.data.model.Post
import com.altruist.ui.components.AltruistBottomBar
import com.altruist.ui.components.CircleButton
import com.altruist.ui.components.CircleButtonWithoutText
import com.altruist.ui.components.DoubleTitle
import com.altruist.ui.components.FilterSmallButton
import com.altruist.ui.components.PostItem
import com.altruist.ui.components.SearchBarAltruist
import com.altruist.ui.theme.YellowSearchScreen
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

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

    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

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
            bottomBar = {
                AltruistBottomBar(
                    selected = "Buscar",
                    onMainMenuClick = onMainMenuClick,
                    onDonateClick = onDonateClick,
                    onSearchClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    }
                )
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
                        },
                        placeholder = "Busca algo más concreto",
                        modifier = Modifier
                            .fillMaxWidth(),
                        backgroundColor = YellowSearchScreen,
                        height = 56.dp,
                        borderWidth = 0.dp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    FlowRow(
                        mainAxisSpacing = 12.dp,
                        crossAxisSpacing = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterSmallButton(
                            text = "Muuuuuuuuuuuuebles",
                            icon = painterResource(id = R.drawable.ic_categories),
                            onClick = onChangeCategoryClick
                        )

                        FilterSmallButton(
                            text = "Valeeeeencia",
                            icon = painterResource(id = R.drawable.ic_location_marker),
                            onClick = onChangeLocationClick
                        )

                        FilterSmallButton(
                            text = "50 km",
                            icon = painterResource(id = R.drawable.ic_range),
                            onClick = onChangeRangeClick
                        )
                    }

                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(posts) { post ->
                            PostItem(
                                post = post,
                                onPostItemClick = { onPostItemClick(post) }
                            )
                        }
                    }
                }
                CircleButtonWithoutText(
                    iconDescription = "Mensajes",
                    icon = R.drawable.ic_chat,
                    size = 70.dp,
                    imageSize = 35.dp,
                    onClick = onMessagesClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 20.dp, bottom = 20.dp)
                )

            }
        }
    }
}
