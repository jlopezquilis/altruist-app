package com.altruist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.altruist.ui.components.AltruistLabeledTextField
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.ui.components.PrimaryButton
import com.altruist.ui.components.SecondaryButton
import com.altruist.viewmodel.RegisterViewModel


@Composable
fun RegisterScreen1(
    viewModel: RegisterViewModel = hiltViewModel(),
    onContinue: () -> Unit
) {
    val nombre by viewModel.nombre.collectAsState()
    val apellidos by viewModel.apellidos.collectAsState()
    val username by viewModel.username.collectAsState()
    val genero by viewModel.genero.collectAsState()

    val generoOpciones = listOf("Masculino", "Femenino", "Otro")

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()

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
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AltruistLabeledTextField(
                        label = "Nombre",
                        value = nombre,
                        onValueChange = viewModel::onNombreChange,
                        placeholder = "Nombre"
                    )

                    AltruistLabeledTextField(
                        label = "Apellidos",
                        value = apellidos,
                        onValueChange = viewModel::onApellidosChange,
                        placeholder = "Apellidos"
                    )

                    AltruistLabeledTextField(
                        label = "Nombre de usuario",
                        value = username,
                        onValueChange = viewModel::onUsernameChange,
                        placeholder = "Nombre de usuario"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Género",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 40.dp)
                        )

                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .padding(end = 30.dp)
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            var expanded by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expanded = true },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = genero.ifEmpty { "Seleccionar" },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (genero.isEmpty()) Color.Gray.copy(alpha = 0.5f) else Color.Black
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar género"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                generoOpciones.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { Text(opcion) },
                                        onClick = {
                                            viewModel.onGeneroChange(opcion)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.15f))

                SecondaryButton(
                    text = "Continuar",
                    onClick = {
                        when {
                            viewModel.nombre.value.isBlank() ||
                                    viewModel.apellidos.value.isBlank() ||
                                    viewModel.username.value.isBlank() ||
                                    viewModel.genero.value.isBlank() -> {
                                viewModel.showError("Por favor, completa todos los campos.")
                            }

                            !viewModel.isUsernameValid() -> {
                                viewModel.showError("El nombre de usuario debe:\n- Tener al menos 4 caracteres\n- Empezar por letra\n- Solo usar letras, números, puntos o guiones bajos")
                            }

                            else -> onContinue()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.isDataValid()
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }
    }
}
