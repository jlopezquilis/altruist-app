package com.altruist.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.R
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.BasicButton
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.TitleMediumTextStyle
import com.altruist.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen3(
    viewModel: RegisterViewModel,
    onRegister3Success: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val inputCode by viewModel.inputCode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val register3Success by viewModel.register3Success.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensaje si hay error
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    if (register3Success) {
        LaunchedEffect(Unit) {
            viewModel.resetRegister3Success()
            viewModel.clearError()
            onRegister3Success()
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
                    modifier = Modifier
                        .padding(horizontal = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Por favor, introduce el código que hemos enviado a tu correo",
                        style = TitleMediumTextStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    AltruistTextField(
                        value = inputCode,
                        onValueChange = viewModel::onInputCodeChange,
                        placeholder = "Código de verificación",
                        textAlign = TextAlign.Center
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¿No te ha llegado?\nEcha un vistazo en spam o prueba a reenviarlo",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        BasicButton(
                            text = "Reenviar Código",
                            onClick = {viewModel.generarYEnviarCodigo(email)}
                        )
                    }

                }

                Spacer(modifier = Modifier.weight(0.15f))

                SecondaryButton(
                    text = if (isLoading) "Cargando..." else "Continuar",
                    onClick = {viewModel.onContinueFromRegister3Click()},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = inputCode.length == 6
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
