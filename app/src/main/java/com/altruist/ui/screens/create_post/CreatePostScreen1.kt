package com.altruist.ui.screens.create_post

import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.altruist.ui.components.AltruistLabeledTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.theme.BackgroundTop
import com.altruist.viewmodel.CreatePostViewModel
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen1(
    viewModel: CreatePostViewModel,
    onPost1Success: () -> Unit
) {
    val title by viewModel.title.collectAsState()
    val category by viewModel.category.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val createPost1Success by viewModel.createPost1Success.collectAsState()

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
            onPost1Success()
        }
    }

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
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to BackgroundTop,
                            0.6f to BackgroundTop,
                            1.0f to BackgroundBottom
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Text(
                        text = "Vamos a crear una publicación",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Column {
                        Text("¿Qué quieres donar?", style = MaterialTheme.typography.labelLarge)
                        Text("Pon un buen título", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        AltruistLabeledTextField(
                            label = "",
                            value = title,
                            onValueChange = viewModel::onTitleChange,
                            placeholder = "Título"
                        )
                    }

                    Column {
                        Text("Elige una categoría", style = MaterialTheme.typography.labelLarge)
                        Text("Así encontrarán mejor tu publicación", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        var expanded by remember { mutableStateOf(false) }
                        val categorias = listOf("Ropa", "Comida", "Muebles", "Juguetes", "Otros")

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFFFB300), shape = RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .clickable { expanded = true }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (category.isEmpty()) "Categoría" else category,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (category.isEmpty()) Color.Gray.copy(alpha = 0.5f) else Color.Black
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }

                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                categorias.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { Text(opcion) },
                                        onClick = {
                                            viewModel.onCategoryChange(opcion)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column {
                        Text("Añade algunas fotos", style = MaterialTheme.typography.labelLarge)
                        Text("Muéstrale al mundo qué quieres donar", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(imageUris) { uri ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeImage(uri) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color(0xFFF0F0F0), shape = CircleShape)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Black)
                                    }
                                }
                            }

                            item {
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFFFB300), shape = RoundedCornerShape(12.dp))
                                        .clickable {
                                            launcher.launch("image/*")
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.Gray, modifier = Modifier.size(36.dp))
                                }
                            }
                        }
                    }
                }

                SecondaryButton(
                    text = "Continuar",
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onContinueFromCreatePost1Click(context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.isDataValid()
                )
            }
        }
    }
}
