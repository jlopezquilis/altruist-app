package com.altruist.ui.screens.create_post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.altruist.ui.components.AltruistBorderedTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.DoubleTitleForTextField
import com.altruist.ui.components.DropDownBorderedAltruist
import com.altruist.ui.components.DropDownCategoryBorderedAltruist
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.utils.enums.PostStatus
import com.altruist.viewmodel.CreatePostViewModel
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen2(
    viewModel: CreatePostViewModel,
    onPost2Success: () -> Unit
) {
    val description by viewModel.description.collectAsState()
    val status by viewModel.status.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val createPost2Success by viewModel.createPost2Success.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
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

    LaunchedEffect(createPost2Success) {
        if (createPost2Success) {
            viewModel.resetCreatePost2Success()
            viewModel.clearError()
            onPost2Success()
        }
    }

    AltruistScreenWrapper (
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
                        .padding(horizontal = 40.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(35.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        // Descripción
                        Column {
                            DoubleTitleForTextField(
                                title1 = "Cuéntanos un poco más",
                                title2 = "Añade una descripción a tu donación"
                            )
                            AltruistBorderedTextField(
                                value = description,
                                onValueChange = viewModel::onDescriptionChange,
                                singleLine = false,
                                textAlign = TextAlign.Start,
                                placeholder = "Descripción",
                                height = 220.dp
                            )
                        }

                        // Estado del artículo
                        Column {
                            DoubleTitleForTextField(
                                title1 = "¿Cuál es el estado de tu artículo?",
                                title2 = "Elige entre las siguientes opciones"
                            )

                            var expanded by remember { mutableStateOf(false) }

                            DropDownBorderedAltruist(
                                itemSelected = status,
                                items = PostStatus.allStatuses,
                                placeholderText = "Estado",
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                onItemSelected = { selectedStatus ->
                                    viewModel.onStateChange(selectedStatus)
                                },
                                modifier = Modifier
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.1f))

                    SecondaryButton(
                        text = "Continuar",
                        onClick = {
                            coroutineScope.launch {
                                viewModel.onContinueFromCreatePost2Click()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.isDataScreen2Valid()
                    )

                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }
}
