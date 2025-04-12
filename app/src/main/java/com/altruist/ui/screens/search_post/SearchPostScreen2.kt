package com.altruist.ui.screens.search_post

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.altruist.ui.components.AltruistBorderedShadowedTextField
import com.altruist.ui.components.AltruistBorderedTextField
import com.altruist.ui.components.DoubleTitleForTextField
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.SearchPostViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun SearchPostScreen2(
    viewModel: SearchPostViewModel,
    onSearchPost2Success: () -> Unit
) {
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()
    val selectedDistanceKm by viewModel.selectedDistanceKm.collectAsState()

    val context = LocalContext.current
    val geocoder = Geocoder(context)
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 12f)
    }

    var searchQuery by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<LatLng?>(null) }

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

    LaunchedEffect(Unit) {
        moveToUserLocation()
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            delay(500)
            viewModel.searchLocation(searchQuery, geocoder) { result ->
                searchResult = result
                result?.let { viewModel.onLocationSelected(it.latitude, it.longitude) }
            }
        }
    }

    LaunchedEffect(latitude, longitude) {
        val newPosition = LatLng(latitude, longitude)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLng(newPosition)
        )
    }

    AltruistScreenWrapper(
        statusBarColor = White,
        navigationBarColor = White
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(White),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(top = 70.dp)
                        .padding(bottom = 30.dp)
                ) {
                    DoubleTitleForTextField(
                        title1 = "¿Dónde lo estás buscando?",
                        title2 = "Selecciona una localización y un rango."
                    )
                }

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

                    AltruistBorderedShadowedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Busca una localización",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 60.dp, vertical = 20.dp)
                            .align(Alignment.TopCenter),
                        height = 56.dp
                    )

                    IconButton(
                        onClick = { moveToUserLocation() },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                            .size(48.dp)
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(vertical = 30.dp)
                ) {
                    Slider(
                        value = selectedDistanceKm,
                        onValueChange = { viewModel.onDistanceSelected(it) },
                        valueRange = 0f..100f
                    )
                    Text(
                        text = "${selectedDistanceKm.toInt()} km",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .padding(bottom = 70.dp)
                ) {
                    SecondaryButton(
                        text = "Continuar",
                        onClick = {
                            viewModel.onContinueFromSearchPost2Click()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
