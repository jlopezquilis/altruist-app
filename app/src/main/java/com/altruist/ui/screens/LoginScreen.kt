package com.altruist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.altruist.R
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import androidx.lifecycle.viewmodel.compose.viewModel
import com.altruist.ui.components.PrimaryButton
import com.altruist.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    //onLoginSuccess: () -> Unit = {}
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginResult by viewModel.loginResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Navegar si login fue exitoso
    LaunchedEffect(loginResult) {
        loginResult?.onSuccess {
            //onLoginSuccess()
            viewModel.showError("Inicio de sesión exitoso: ${it.name}")
        }?.onFailure {
            viewModel.showError("Error al iniciar sesión: ${it.message}")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to BackgroundTop,
                        0.4f to BackgroundTop,
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
            Spacer(modifier = Modifier.weight(0.20f))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Altruist",
                modifier = Modifier.size(230.dp)
            )

            Spacer(modifier = Modifier.weight(0.10f))

            // Inputs con sus etiquetas pegadas
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("E-mail o nombre de usuario", style = MaterialTheme.typography.labelLarge)
                    AltruistTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = "E-mail o nombre de usuario"
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Contraseña", style = MaterialTheme.typography.labelLarge)
                    AltruistTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = "Contraseña",
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            }

            // Error message
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.10f))

            PrimaryButton(
                text = if (isLoading) "Cargando..." else "Iniciar Sesión",
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}
