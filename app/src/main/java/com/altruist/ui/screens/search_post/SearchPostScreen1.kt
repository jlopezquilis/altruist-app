package com.altruist.ui.screens.search_post

import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.data.model.Category
import com.altruist.viewmodel.SharedViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.SearchPostViewModel
import com.altruist.ui.components.CategoryButton
import com.altruist.ui.components.DoubleTitle

@Composable
fun SearchPostScreen1(
    viewModel: SearchPostViewModel,
    onSearchPost1Success: () -> Unit
) {
    val sharedViewModel: SharedViewModel = hiltViewModel()

    val searchPost1Success by viewModel.searchPost1Success.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage by viewModel.errorMessage.collectAsState()

    val categories by sharedViewModel.categories.collectAsState()
    val categoriesWithAll = listOf(
        Category(
            id_category = -1L,
            name = "Todo",
            description = "Buscar en todas las categorías",
            icon_url = "https://firebasestorage.googleapis.com/v0/b/altruist-app-73b03.firebasestorage.app/o/category_icons%2Finfinito.png?alt=media&token=41234f86-3f4e-4ce8-bd2a-967c0a957274"
        )
    ) + categories

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(searchPost1Success) {
        if (searchPost1Success) {
            viewModel.resetSearchPost1Success()
            viewModel.clearError()
            onSearchPost1Success()
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
            containerColor = Color.Transparent
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
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    DoubleTitle(
                        title1 = "¿Qué estas buscando?",
                        title2 = "Elige una categoría para empezar"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(25.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxHeight(),
                            contentPadding = PaddingValues(vertical = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(25.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            items(categoriesWithAll) { category ->
                                CategoryButton(
                                    category = category,
                                    onClick = {
                                        viewModel.onCategoryChange(category)
                                        viewModel.onContinueFromSearchPost1Click()},
                                    isMainCategory = category.id_category == -1L
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
