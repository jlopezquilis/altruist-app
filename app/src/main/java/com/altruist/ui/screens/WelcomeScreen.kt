package com.altruist.ui.screens

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
import com.altruist.ui.components.PrimaryButton
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundBottom

import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect
import com.altruist.utils.AltruistScreenWrapper


@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    AltruistScreenWrapper (
        statusBarColor = BackgroundTop,
        navigationBarColor = BackgroundBottom
    ) {
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
                Spacer(modifier = Modifier.weight(0.2f)) // 20% de alto

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Altruist",
                    modifier = Modifier.size(230.dp)
                )

                Spacer(modifier = Modifier.weight(0.45f)) // Espacio entre logo y botones

                // Botones
                Column(
                    verticalArrangement = Arrangement.spacedBy(35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    PrimaryButton(text = "Iniciar Sesión", onClick = onLoginClick)
                    SecondaryButton(text = "Registrarse", onClick = onRegisterClick)
                }

                Spacer(modifier = Modifier.weight(0.35f)) // 35% desde bottom
            }
        }
    }
}


