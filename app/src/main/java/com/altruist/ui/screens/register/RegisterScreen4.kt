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
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.AltruistTextField
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.TitleMediumTextStyle
import com.altruist.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen4(
    viewModel: RegisterViewModel,
    onRegister4Success: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val situation by viewModel.situation.collectAsState()
    val anonymous by viewModel.anonymous.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val register4Success by viewModel.register4Success.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar error si hay
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    if (register4Success) {
        LaunchedEffect(Unit) {
            viewModel.resetRegister4Success()
            viewModel.clearError()
            onRegister4Success()
        }
    }

    LaunchedEffect(name) {
        println("✅ Nombre recibido en RegisterScreen4: $name")
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
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Hola, ${name}!",
                        style = TitleMediumTextStyle)
                    Text(
                        text = "Describe tu situación o la intención con la que vas a utilizar la aplicación para que otros usuarios te conozcan un poco más.",
                        textAlign = TextAlign.Center
                    )

                    AltruistTextField(
                        value = situation,
                        onValueChange = viewModel::onSituationChange,
                        placeholder = "Opcional",
                        textAlign = TextAlign.Center,
                        singleLine = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "¿Quieres que tu perfil sea anónimo?")
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "Anónimo")
                            Switch(
                                checked = anonymous,
                                onCheckedChange = viewModel::onAnonymousChange,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFFE29B1F),
                                    uncheckedThumbColor = Color.LightGray,
                                    uncheckedTrackColor = Color.White // o el que tú prefieras
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.10f))

                SecondaryButton(
                    text = "Continuar",
                    onClick = {viewModel.onContinueFromRegister4Click()},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
