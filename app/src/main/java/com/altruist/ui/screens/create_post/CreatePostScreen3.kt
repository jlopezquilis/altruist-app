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
import com.google.android.gms.maps.CameraUpdateFactory
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
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val createPost3Success by viewModel.createPost3Success.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val geocoder = Geocoder(context)

    var searchQuery by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<LatLng?>(null) }

    // Valencia ubicación por defecto
    val valenciaLatLng = LatLng(39.4699, -0.3763)
    // Inicializa el mapa con Valencia o la ubicación actual del ViewModel
    val initialLatLng = if (latitude == 0.0 && longitude == 0.0) valenciaLatLng else LatLng(latitude, longitude)


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 12f)
    }


    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(errorMessage!!)
        }
    }

    LaunchedEffect(createPost3Success) {
        if (createPost3Success) {
            viewModel.resetCreatePost3Success()
            viewModel.clearError()
            onPost3Success()
        }
    }

    // Cuando encuentra ubicación nueva
    LaunchedEffect(searchResult) {
        searchResult?.let { latLng ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            )
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

                    LaunchedEffect(searchQuery) {
                        if (searchQuery.isNotBlank()) {
                            kotlinx.coroutines.delay(500) // Espera 500ms después de escribir
                            viewModel.searchLocation(searchQuery, geocoder) { result ->
                                searchResult = result
                            }
                        }
                    }

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            searchResult = latLng
                            viewModel.onLocationSelected(latLng.latitude, latLng.longitude)
                        }
                    ) {
                        val markerPosition = searchResult ?: LatLng(latitude, longitude)

                        Marker(
                            state = MarkerState(position = markerPosition),
                            title = "Ubicación seleccionada"
                        )

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