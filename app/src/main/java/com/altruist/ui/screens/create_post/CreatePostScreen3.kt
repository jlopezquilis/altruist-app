package com.altruist.ui.screens.create_post

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.ui.Alignment
import androidx.annotation.RequiresPermission
import com.altruist.ui.components.SecondaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.altruist.ui.components.AltruistBorderedTextField
import com.altruist.ui.components.AltruistLabeledTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.components.DoubleTitleForTextField
import com.altruist.ui.components.SearchBarAltruist
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.CreatePostViewModel
import com.google.android.gms.location.LocationServices
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
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<LatLng?>(null) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val valenciaLatLng = LatLng(39.4699, -0.3763)
    val initialLatLng = if (latitude == 0.0 && longitude == 0.0) valenciaLatLng else LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 12f)
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun moveToUserLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    searchResult = userLatLng
                    viewModel.onLocationSelected(it.latitude, it.longitude)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            kotlinx.coroutines.delay(500)
            viewModel.searchLocation(searchQuery, geocoder) { result ->
                searchResult = result
                result?.let { viewModel.onLocationSelected(it.latitude, it.longitude) }
            }
        }
    }

    // Actualizar mapa cuando cambian lat/lon
    LaunchedEffect(latitude, longitude) {
        val newPosition = LatLng(latitude, longitude)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLng(newPosition)
        )
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            SnackbarHostState().showSnackbar(it)
        }
    }

    LaunchedEffect(createPost3Success) {
        if (createPost3Success) {
            viewModel.resetCreatePost3Success()
            viewModel.clearError()
            onPost3Success()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(White),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // PARTE 1: Textos
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(top = 70.dp)
                        .padding(bottom = 30.dp)
                ) {
                    DoubleTitleForTextField(
                        title1 = "¿Dónde quieres donarlo?",
                        title2 = "Elige una ubicación"
                    )
                }

                // PARTE 2: Mapa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
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

                    SearchBarAltruist (
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Busca una localización",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 60.dp, vertical = 20.dp)
                            .align(Alignment.TopCenter),
                        backgroundColor = Color.White,
                        height = 56.dp,
                        borderWidth = 1.dp
                    )

                    IconButton(
                        onClick = { moveToUserLocation() },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                            .size(48.dp) // tamaño del botón redondo
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Mi ubicación",
                            tint = Color.Black
                        )
                    }
                }

                // PARTE 3: Botón continuar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(top = 50.dp)
                        .padding(bottom = 70.dp)
                ) {
                    SecondaryButton(
                        text = if (isLoading) "Cargando..." else "Continuar",
                        onClick = { viewModel.onContinueFromCreatePost3Click() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    )
                }
            }
        }
    }
}