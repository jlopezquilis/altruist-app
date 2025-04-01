package com.altruist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.R
import com.altruist.ui.components.AltruistLabeledTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.components.SecondaryButton
import com.altruist.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen2(
    viewModel: RegisterViewModel = hiltViewModel(),
    onContinue: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val repeatPassword by viewModel.repeatPassword.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val codigoEnviadoOk by viewModel.codigoEnviadoConExito.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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

    LaunchedEffect(codigoEnviadoOk) {
        if (codigoEnviadoOk) {
            onContinue()
            viewModel.resetCodigoEnviado() // para evitar navegación doble
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
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AltruistLabeledTextField(
                        label = "E-mail",
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = "E-mail"
                    )

                    AltruistLabeledTextField(
                        label = "Contraseña",
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = "Contraseña",
                        visualTransformation = PasswordVisualTransformation()
                    )

                    AltruistLabeledTextField(
                        label = "Repita su contraseña",
                        value = repeatPassword,
                        onValueChange = viewModel::onRepeatPasswordChange,
                        placeholder = "Repita su contraseña",
                        visualTransformation = PasswordVisualTransformation()
                    )
                }

                Spacer(modifier = Modifier.weight(0.15f))

                SecondaryButton(
                    text = if (isLoading) "Cargando..." else "Continuar",
                    onClick = {viewModel.onContinueFromRegister2Click()},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
