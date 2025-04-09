package com.altruist.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.altruist.R
import com.altruist.ui.components.CircleButton
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom
import com.altruist.utils.AltruistScreenWrapper

@Composable
fun MainMenuScreen(
    onDonarClick: () -> Unit,
    onBuscarClick: () -> Unit,
    onMisDonacionesClick: () -> Unit,
    onMensajesClick: () -> Unit
) {

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
                            onClick = onDonarClick
                        )
                        CircleButton(
                            "Quiero Buscar",
                            icon = R.drawable.ic_lupa,
                            size = 90.dp,
                            imageSize = 50.dp,
                            onClick = onBuscarClick
                        )
                    }

                    // Botón central
                    CircleButton(
                        "Mis donaciones",
                        icon = R.drawable.ic_post,
                        size = 90.dp,
                        imageSize = 50.dp,
                        onClick = onMisDonacionesClick
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
                            onClick = onMensajesClick
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
    }
}

