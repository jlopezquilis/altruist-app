package com.altruist.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.R
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import androidx.lifecycle.viewmodel.compose.viewModel
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.PrimaryButton
import com.altruist.ui.theme.ErrorTextStyle
import com.altruist.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val user by viewModel.currentUser.collectAsState(initial = null)

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

    // Navegar si login fue exitoso
    LaunchedEffect(loginSuccess) {
        if (loginSuccess){
            onLoginSuccess()
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
                Spacer(modifier = Modifier.weight(0.10f))

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
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "E-mail",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        AltruistTextField(
                            value = email,
                            onValueChange = viewModel::onEmailChange,
                            placeholder = "E-mail"
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "Contraseña",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        AltruistTextField(
                            value = password,
                            onValueChange = viewModel::onPasswordChange,
                            placeholder = "Contraseña",
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(0.10f))

                PrimaryButton(
                    text = if (isLoading) "Cargando..." else "Iniciar Sesión",
                    onClick = { viewModel.onLoginClick() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}

