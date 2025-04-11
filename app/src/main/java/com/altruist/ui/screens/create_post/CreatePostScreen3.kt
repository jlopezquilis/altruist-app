package com.altruist.ui.screens.create_post

import android.location.Geocoder
import com.altruist.ui.components.SecondaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.altruist.ui.components.AltruistBorderedTextField
import com.altruist.ui.components.AltruistLabeledTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.components.DoubleTitleForTextField
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.CreatePostViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@Composable
fun CreatePostScreen3(
    viewModel: CreatePostViewModel,
    onPost3Success: () -> Unit
) {
    val latitude by viewModel.latitude.collectAsState()  // Latitud
    val longitude by viewModel.longitude.collectAsState()  // Longitud
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val createPost3Success by viewModel.createPost3Success.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Estado de la búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Estado para manejar el resultado de la búsqueda
    var searchResult by remember { mutableStateOf<LatLng?>(null) }

    // Crear Geocoder con el contexto actual
    val geocoder = Geocoder(LocalContext.current)

    // Estado para el enfoque del TextField
    var textFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(createPost3Success) {
        if (createPost3Success) {
            viewModel.resetCreatePost3Success()
            viewModel.clearError()
            onPost3Success()
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

                    DoubleTitleForTextField(
                        title1 = "¿Dónde quieres publicarlo?",
                        title2 = "Elige una ubicación")

                    // Barra de búsqueda
                    AltruistBorderedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Buscar ubicación",
                        modifier = Modifier.fillMaxWidth(),
                        height = 56.dp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // Cuando el texto cambia, buscamos la ubicación
                    LaunchedEffect(searchQuery) {
                        if (searchQuery.isNotEmpty()) {
                            viewModel.searchLocation(searchQuery, geocoder) { result ->
                                searchResult = result
                            }
                        }
                    }

                    // Mapa
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(
                            LatLng(latitude ?: 39.4699, longitude ?: -0.3763), 10f
                        )
                    }

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f),  // El mapa ocupa toda la pantalla
                        cameraPositionState = cameraPositionState
                    ) {
                        // Detectar si el usuario ha movido el mapa
                        val currentPosition = cameraPositionState.position.target

                        // Si tenemos un resultado de búsqueda, lo mostramos en el mapa
                        searchResult?.let { latLng ->
                            Marker(
                                state = MarkerState(position = latLng),
                                title = "Ubicación seleccionada"
                            )

                            // Si el usuario mueve el mapa, actualizamos el marcador
                            if (currentPosition != latLng) {
                                searchResult = LatLng(currentPosition.latitude, currentPosition.longitude)
                            }

                            // Actualizamos la cámara para centrar el mapa en la nueva ubicación
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                        }
                    }


                    Spacer(modifier = Modifier.weight(0.1f))

                    SecondaryButton(
                        text = if (isLoading) "Cargando..." else "Continuar",
                        onClick = {viewModel.onContinueFromCreatePost3Click()},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }
}