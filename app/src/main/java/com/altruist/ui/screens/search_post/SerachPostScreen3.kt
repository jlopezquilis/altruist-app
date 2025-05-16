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
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.R
import com.altruist.data.model.Category
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
import com.altruist.viewmodel.SharedViewModel
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

@Composable
fun SearchPostScreen3(
    viewModel: SearchPostViewModel,
    onChangeLocationClick: () -> Unit,
    onChangeRangeClick: () -> Unit,
    onPostItemClick: (Post) -> Unit,
    onMainMenuClick: () -> Unit,
    onDonateClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    val sharedViewModel: SharedViewModel = hiltViewModel()

    val isLoading by viewModel.isLoading.collectAsState()

    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedDistanceKm by viewModel.selectedDistanceKm.collectAsState()
    val locationName by viewModel.locationName.collectAsState()

    val posts by viewModel.filteredPosts.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

    val categories by sharedViewModel.categories.collectAsState()
    val categoriesWithAll = listOf(
        Category(
            id_category = -1L,
            name = "Todo",
            description = "Buscar en todas las categorías",
            icon_url = "https://firebasestorage.googleapis.com/v0/b/altruist-app-73b03.firebasestorage.app/o/category_icons%2Finfinito.png?alt=media&token=41234f86-3f4e-4ce8-bd2a-967c0a957274"
        )
    ) + categories
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()

    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

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

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
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

                        SearchBarAltruist(
                            value = searchQuery,
                            onValueChange = { newSearchQuery ->
                                viewModel.onSearchQueryChange(newSearchQuery)
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
                            Box {
                                selectedCategory?.let {
                                    FilterSmallButton(
                                        text = it.name,
                                        icon = painterResource(id = R.drawable.ic_categories),
                                        onClick = {
                                            isDropdownExpanded = true
                                        }
                                    )
                                }

                                DropdownMenu(
                                    expanded = isDropdownExpanded,
                                    onDismissRequest = { isDropdownExpanded = false }
                                ) {
                                    categoriesWithAll.forEach { category ->
                                        DropdownMenuItem(
                                            text = { Text(category.name) },
                                            onClick = {
                                                viewModel.onCategoryChange(category)
                                                viewModel.loadFilteredPosts(
                                                    onSuccess = {},
                                                    onError = { error -> viewModel.showError(error) }
                                                )
                                                isDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }


                            FilterSmallButton(
                                text = locationName,
                                icon = painterResource(id = R.drawable.ic_location_marker),
                                onClick = onChangeLocationClick
                            )

                            FilterSmallButton(
                                text = selectedDistanceKm.toInt().toString(),
                                icon = painterResource(id = R.drawable.ic_range),
                                onClick = onChangeRangeClick
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (posts.isEmpty()) {
                            Text(
                                text = "No se han encontrado publicaciones",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 40.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else {
                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(vertical = 5.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                items(posts) { post ->
                                    PostItem(
                                        viewModel = viewModel,
                                        post = post,
                                        onPostItemClick = { onPostItemClick(post) }
                                    )
                                }
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
}
