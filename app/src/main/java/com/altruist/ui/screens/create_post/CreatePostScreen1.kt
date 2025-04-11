package com.altruist.ui.screens.create_post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.altruist.R
import com.altruist.ui.components.AltruistBorderedTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.DoubleTitleForTextField
import com.altruist.ui.components.DropDownCategoryBorderedAltruist
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.LightGray
import com.altruist.ui.theme.Shapes
import com.altruist.ui.theme.White
import com.altruist.ui.theme.YellowDark
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.CreatePostViewModel
import com.altruist.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen1(
    viewModel: CreatePostViewModel,
    onPost1Success: () -> Unit
) {
    val sharedViewModel: SharedViewModel = hiltViewModel()

    val title by viewModel.title.collectAsState()
    val category by viewModel.category.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val createPost1Success by viewModel.createPost1Success.collectAsState()
    val categories by sharedViewModel.categories.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onAddImageClick(uri)
    }

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

    LaunchedEffect(createPost1Success) {
        if (createPost1Success) {
            viewModel.resetCreatePost1Success()
            viewModel.clearError()
            onPost1Success()
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
                    Text(
                        text = "Vamos a crear una publicación",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(35.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {


                        Column {
                            DoubleTitleForTextField(
                                title1 = "¿Qué quieres donar?",
                                title2 = "Pon un buen título"
                            )
                            AltruistBorderedTextField(
                                value = title,
                                onValueChange = viewModel::onTitleChange,
                                singleLine = true,
                                textAlign = TextAlign.Start,
                                placeholder = "Título",
                                height = 56.dp
                            )
                        }

                        Column {
                            DoubleTitleForTextField(
                                title1 = "Elige una categoría",
                                title2 = "Así encontrarán mejor tu publicación"
                            )

                            var expanded by remember { mutableStateOf(false) }
                            
                            DropDownCategoryBorderedAltruist(
                                category = category,
                                categories = categories,
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                onCategorySelected = { selectedCategory ->
                                    viewModel.onCategoryChange(selectedCategory)
                                }
                            )
                        }

                        Column {
                            DoubleTitleForTextField(
                                title1 = "Añade algunas fotos",
                                title2 = "Muéstrale al mundo qué quieres donar"
                            )

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(imageUris) { uri ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(uri),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(90.dp)
                                                .clip(Shapes.medium)
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                        IconButton(
                                            onClick = { viewModel.removeImage(uri) },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(LightGray, shape = CircleShape)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription = "Eliminar",
                                                modifier = Modifier
                                                    .size(20.dp)
                                            )
                                        }
                                    }
                                }

                                item {
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, YellowDark, shape = Shapes.medium)
                                            .clickable {
                                                launcher.launch("image/*")
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_add_gray),
                                            contentDescription = "Añadir",
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.1f))

                    SecondaryButton(
                        text = if (isLoading) "Cargando..." else "Continuar",
                        onClick = {viewModel.onContinueFromCreatePost1Click(context)},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && viewModel.isDataScreen1Valid()
                    )

                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }
}
