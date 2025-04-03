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
import androidx.compose.ui.platform.LocalContext
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
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage


@Composable
fun RegisterScreen5(
    viewModel: RegisterViewModel,
    onRegister5Success: () -> Unit
) {
    val selectedImageUri by viewModel.profilePictureUrl.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val imageUri by viewModel.profilePictureUrl.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setProfilePictureUrl(uri)
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

                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sube una imagen para tu foto de perfil",
                        style = TitleMediumTextStyle,
                        textAlign = TextAlign.Center
                    )

                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray)
                        )
                    }

                    Button(
                        onClick = { launcher.launch("image/*") },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Cambiar", color = Color.Black)
                    }

                }

                Spacer(modifier = Modifier.weight(0.1f))

                SecondaryButton(
                    text = "Continuar",
                    onClick = {viewModel.onContinueFromRegister5Click()},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
