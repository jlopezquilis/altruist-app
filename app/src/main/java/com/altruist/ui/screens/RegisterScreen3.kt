package com.altruist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.R
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.components.PrimaryButton
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.TitleMediumTextStyle
import com.altruist.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen3(
    viewModel: RegisterViewModel = hiltViewModel(),
    onCodeValidated: () -> Unit
) {
    val verificationCode by viewModel.verificationCode.collectAsState()
    val inputCode by viewModel.inputCode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

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
                }

                Spacer(modifier = Modifier.weight(0.15f))

                SecondaryButton(
                    text = "Continuar",
                    onClick = {
                        if (inputCode == verificationCode) {
                            onCodeValidated()
                        } else {
                            viewModel.showError("El código no es correcto.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = inputCode.length == 6
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
