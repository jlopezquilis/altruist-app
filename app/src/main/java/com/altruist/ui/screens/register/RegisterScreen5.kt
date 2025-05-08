package com.altruist.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.altruist.R
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.TitleMediumTextStyle
import com.altruist.viewmodel.RegisterViewModel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.layout.ContentScale
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.altruist.ui.components.BasicButton


@Composable
fun RegisterScreen5(
    viewModel: RegisterViewModel,
    onRegister5Success: () -> Unit
) {
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val register5Success by viewModel.register5Success.collectAsState()

    val imageUri by viewModel.profilePictureUri.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val loginSuccess by viewModel.loginSuccess.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onProfilePictureUriChange(uri)
    }

    // Mostrar snackbar si hay error
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    if (register5Success && loginSuccess) {
        LaunchedEffect(Unit) {
            viewModel.resetRegister4Success()
            viewModel.clearError()
            onRegister5Success()
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
                    .padding(horizontal = 40.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.05f))

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Altruist",
                    modifier = Modifier.size(230.dp)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sube una imagen\npara tu foto de perfil",
                        style = TitleMediumTextStyle,
                        textAlign = TextAlign.Center
                    )

                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile_pic),
                            contentDescription = "Imagen por defecto",
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    BasicButton(
                        text = "Cambiar",
                        onClick = { launcher.launch("image/*") }
                    )

                }

                Spacer(modifier = Modifier.weight(0.1f))

                SecondaryButton(
                    text = if (isLoading) "Cargando..." else "Finalizar",
                    onClick = {viewModel.onContinueFromRegister5Click(context)},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
