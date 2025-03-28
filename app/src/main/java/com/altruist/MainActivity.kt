package com.altruist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.altruist.ui.screens.WelcomeScreen
import com.altruist.ui.theme.AltruistTheme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.altruist.ui.screens.LoginScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AltruistApp()
        }
    }
}

object NavRoutes {
    const val Welcome = "welcome"
    const val Login = "login"
}


@Composable
fun AltruistApp() {
    val navController = rememberNavController()

    AltruistTheme {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Welcome
        ) {
            composable(NavRoutes.Welcome) {
                WelcomeScreen(
                    onLoginClick = {
                        navController.navigate(NavRoutes.Login)
                    },
                    onRegisterClick = {
                        // Puedes añadir aquí navegación al registro
                    }
                )
            }

            composable(NavRoutes.Login) {
                LoginScreen(
                    /*onLoginSuccess = {
                        // Ejemplo: ir a Home o mostrar mensaje
                        println("Login correcto")
                    }
                     */
                )
            }


        }
    }
}


