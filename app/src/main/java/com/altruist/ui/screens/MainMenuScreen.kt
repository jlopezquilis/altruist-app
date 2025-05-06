package com.altruist.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.altruist.R
import com.altruist.ui.components.CircleButton
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.MainMenuViewModel

@Composable
fun MainMenuScreen(
    onDonarClick: () -> Unit,
    onBuscarClick: () -> Unit,
    onMisDonacionesClick: () -> Unit,
    onMensajesClick: () -> Unit,
    navController: NavController,
    viewModel: MainMenuViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }


    AltruistScreenWrapper (
        statusBarColor = BackgroundTop,
        navigationBarColor = BackgroundBottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
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
            // Botón de logout en la esquina superior derecha
            IconButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Cerrar sesión",
                    modifier = Modifier.size(28.dp),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
            }

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
                    // Fila de botones superiores
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircleButton(
                            "Quiero Donar",
                            icon = R.drawable.ic_donar,
                            size = 90.dp,
                            imageSize = 50.dp,
                            onClick = onDonarClick,
                            modifier = Modifier
                        )
                        CircleButton(
                            "Quiero Buscar",
                            icon = R.drawable.ic_lupa,
                            size = 90.dp,
                            imageSize = 50.dp,
                            onClick = onBuscarClick,
                            modifier = Modifier
                        )
                    }

                    // Botón central
                    CircleButton(
                        "Mis donaciones",
                        icon = R.drawable.ic_post,
                        size = 90.dp,
                        imageSize = 50.dp,
                        onClick = onMisDonacionesClick,
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Fila de botones inferiores
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircleButton(
                            "Mensajes",
                            icon = R.drawable.ic_chat,
                            size = 70.dp,
                            imageSize = 35.dp,
                            onClick = onMensajesClick,
                            modifier = Modifier
                        )/*
                    CircleButton("Perfil",
                        icon = R.drawable.menu_profile,
                        size = 70.dp,
                        imageSize = 35.dp,
                        onClick = onPerfilClick
                    )
                    */
                    }
                }
                Spacer(modifier = Modifier.weight(0.10f))
            }
        }
        // AlertDialog de confirmación
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("¿Cerrar sesión?") },
                text = { Text("¿Confirmas que quieres cerrar la sesión?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

