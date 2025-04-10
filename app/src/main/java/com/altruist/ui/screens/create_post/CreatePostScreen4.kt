package com.altruist.ui.screens.create_post

import android.location.Geocoder
import androidx.compose.foundation.Image
import com.altruist.ui.components.SecondaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.altruist.R
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
fun CreatePostScreen4(
    viewModel: CreatePostViewModel,
    onPost4Success: () -> Unit
) {
    val errorMessage by viewModel.errorMessage.collectAsState()
    val createPost4Success by viewModel.createPost4Success.collectAsState()

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

    LaunchedEffect(createPost4Success) {
        if (createPost4Success) {
            viewModel.resetCreatePost3Success()
            viewModel.clearError()
            onPost4Success()
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
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(0.1f))
                    Text(
                        text = "¡Se ha creado tu\npublicación!",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_done),
                        contentDescription = "Done",
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.weight(0.1f))

                    SecondaryButton(
                        text = "Volver a Inicio",
                        onClick = {
                            viewModel.clearError()
                            coroutineScope.launch {
                                viewModel.onContinueFromCreatePost4Click()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true
                    )

                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }
}