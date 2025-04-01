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
import com.altruist.ui.screens.RegisterScreen1
import com.altruist.ui.screens.RegisterScreen2
import com.altruist.ui.screens.RegisterScreen3
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    const val Register1 = "register1"
    const val Register2 = "register2"
    const val Register3 = "register3"
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
                        navController.navigate(NavRoutes.Register1)
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

            composable(NavRoutes.Register1) {
                RegisterScreen1(
                    onContinue = {
                        navController.navigate(NavRoutes.Register2)
                    }
                )
            }

            composable(NavRoutes.Register2) {
                RegisterScreen2(
                    onContinue = {
                        navController.navigate(NavRoutes.Register3)
                    }
                )
            }

            composable(NavRoutes.Register3) {
                RegisterScreen3(
                    onCodeValidated = {
                        navController.navigate(NavRoutes.Register2)
                    }
                )
            }


        }
    }
}


