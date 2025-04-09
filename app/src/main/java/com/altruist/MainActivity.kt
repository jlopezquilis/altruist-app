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
import com.altruist.ui.screens.create_post.CreatePostScreen1
import com.altruist.ui.screens.create_post.CreatePostScreen2
import com.altruist.ui.screens.main.MainMenuScreen
import com.altruist.ui.screens.register.RegisterScreen1
import com.altruist.ui.screens.register.RegisterScreen2
import com.altruist.ui.screens.register.RegisterScreen3
import com.altruist.ui.screens.register.RegisterScreen4
import com.altruist.ui.screens.register.RegisterScreen5
import com.altruist.viewmodel.CreatePostViewModel
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

    const val MAINMENU = "mainMenu"

    const val CreatePostGraph = "createPost"
    const val CreatePost1 = "createPost1"
    const val CreatePost2 = "createPost2"
    const val CreatePost3 = "createPost3"
}


@Composable
fun AltruistApp() {
    val navController = rememberNavController()

    AltruistTheme {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.CreatePostGraph
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
                    onLoginSuccess = {
                        navController.navigate(NavRoutes.MAINMENU)
                    }
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
                            //navController.navigate(NavRoutes.Register3)
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

            composable(NavRoutes.MAINMENU) {
                MainMenuScreen(
                        onDonarClick = {
                            navController.navigate(NavRoutes.CreatePostGraph)
                        },
                        onBuscarClick = {
                            navController.navigate(NavRoutes.MAINMENU)
                        },
                        onMisDonacionesClick = {
                            navController.navigate(NavRoutes.MAINMENU)
                        },
                        onMensajesClick = {
                            navController.navigate(NavRoutes.MAINMENU)
                        }
                )
            }

            navigation(
                startDestination = NavRoutes.CreatePost1,
                route = NavRoutes.CreatePostGraph
            ) {
                composable(NavRoutes.CreatePost1) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.CreatePostGraph)
                    }
                    val viewModel: CreatePostViewModel = hiltViewModel(parentEntry)
                    CreatePostScreen1(
                        viewModel = viewModel,
                        onPost1Success = {
                            navController.navigate(NavRoutes.CreatePost2)
                        }
                    )
                }

                composable(NavRoutes.CreatePost2) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.CreatePostGraph)
                    }
                    val viewModel: CreatePostViewModel = hiltViewModel(parentEntry)
                    CreatePostScreen2(
                        viewModel = viewModel,
                        onPost2Success = {
                            navController.navigate(NavRoutes.CreatePost3)
                        }
                    )
                }
            }
        }
    }
}


