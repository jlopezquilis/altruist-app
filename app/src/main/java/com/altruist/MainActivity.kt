package com.altruist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.ui.screens.WelcomeScreen
import com.altruist.ui.theme.AltruistTheme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.altruist.ui.screens.LoginScreen
import com.altruist.ui.screens.register.RegisterScreen1
import com.altruist.ui.screens.register.RegisterScreen2
import com.altruist.ui.screens.register.RegisterScreen3
import com.altruist.ui.screens.register.RegisterScreen4
import com.altruist.ui.screens.register.RegisterScreen5
import com.altruist.viewmodel.RegisterViewModel
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

    const val RegisterGraph = "register"
    const val Register1 = "register1"
    const val Register2 = "register2"
    const val Register3 = "register3"
    const val Register4 = "register4"
    const val Register5 = "register5"
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
                        navController.navigate(NavRoutes.RegisterGraph)
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

            navigation(
                startDestination = NavRoutes.Register1,
                route = NavRoutes.RegisterGraph
            ) {
                composable(NavRoutes.Register1) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.RegisterGraph)
                    }
                    val viewModel: RegisterViewModel = hiltViewModel(parentEntry)
                    RegisterScreen1(
                        viewModel = viewModel,
                        onRegister1Success = {
                            navController.navigate(NavRoutes.Register2)
                        }
                    )
                }

                composable(NavRoutes.Register2) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.RegisterGraph)
                    }
                    val viewModel: RegisterViewModel = hiltViewModel(parentEntry)

                    RegisterScreen2(
                        viewModel = viewModel,
                        onRegister2Success = {
                            navController.navigate(NavRoutes.Register3)
                        }
                    )
                }

                composable(NavRoutes.Register3) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.RegisterGraph)
                    }
                    val viewModel: RegisterViewModel = hiltViewModel(parentEntry)

                    RegisterScreen3(
                        viewModel = viewModel,
                        onRegister3Success = {
                            navController.navigate(NavRoutes.Register4)
                        }
                    )
                }

                composable(NavRoutes.Register4) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.RegisterGraph)
                    }
                    val viewModel: RegisterViewModel = hiltViewModel(parentEntry)

                    RegisterScreen4(
                        viewModel = viewModel,
                        onRegister4Success = {
                            navController.navigate(NavRoutes.Register5)
                        }
                    )
                }

                composable(NavRoutes.Register5) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.RegisterGraph)
                    }
                    val viewModel: RegisterViewModel = hiltViewModel(parentEntry)

                    RegisterScreen5(
                        viewModel = viewModel,
                        onRegister5Success = {
                            navController.navigate(NavRoutes.Welcome)
                        }
                    )
                }
            }
        }
    }
}


