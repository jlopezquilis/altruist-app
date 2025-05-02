package com.altruist

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.ui.screens.WelcomeScreen
import com.altruist.ui.theme.AltruistTheme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.altruist.ui.screens.LocationPermissionScreen
import com.altruist.ui.screens.LoginScreen
import com.altruist.ui.screens.create_post.CreatePostScreen1
import com.altruist.ui.screens.create_post.CreatePostScreen2
import com.altruist.ui.screens.create_post.CreatePostScreen3
import com.altruist.ui.screens.create_post.CreatePostScreen4
import com.altruist.ui.screens.main.MainMenuScreen
import com.altruist.ui.screens.post_detail.PostDetailScreen
import com.altruist.ui.screens.register.RegisterScreen1
import com.altruist.ui.screens.register.RegisterScreen2
import com.altruist.ui.screens.register.RegisterScreen3
import com.altruist.ui.screens.register.RegisterScreen4
import com.altruist.ui.screens.register.RegisterScreen5
import com.altruist.ui.screens.search_post.SearchPostScreen1
import com.altruist.ui.screens.search_post.SearchPostScreen2
import com.altruist.ui.screens.search_post.SearchPostScreen3
import com.altruist.ui.screens.user_posts.UserPostApplicantsScreen
import com.altruist.ui.screens.user_posts.UserPostsScreen
import com.altruist.utils.LocationUtils
import com.altruist.viewmodel.CreatePostViewModel
import com.altruist.viewmodel.RegisterViewModel
import com.altruist.viewmodel.SearchPostViewModel
import com.altruist.viewmodel.SharedViewModel
import com.altruist.viewmodel.UserPostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedViewModel: SharedViewModel = hiltViewModel()
            AltruistApp(sharedViewModel)
        }
    }
}

object NavRoutes {
    const val Welcome = "welcome"
    const val Login = "login"
    const val LocationPermission = "locationPermission"

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
    const val CreatePost4 = "createPost4"

    const val SEARCHPOSTGRAPH = "searchPost"
    const val SEARCHPOST1 = "searchPost1"
    const val SEARCHPOST2 = "searchPost2"
    const val SEARCHPOST3 = "searchPost3"

    const val POSTDETAIL = "postDetail"

    const val USERPOSTSGRAPH = "userPosts"
    const val USERPOSTS1 = "userPosts1"
    const val USERPOSTS2 = "userPosts2"

}


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AltruistApp(sharedViewModel: SharedViewModel) {
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
                            navController.navigate(NavRoutes.Welcome){
                                popUpTo(NavRoutes.RegisterGraph) { inclusive = true }
                            }
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
                            navController.navigate(NavRoutes.SEARCHPOSTGRAPH)
                        },
                        onMisDonacionesClick = {
                            navController.navigate(NavRoutes.USERPOSTSGRAPH)
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

                composable(NavRoutes.CreatePost3) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.CreatePostGraph)
                    }
                    val viewModel: CreatePostViewModel = hiltViewModel(parentEntry)
                    CreatePostScreen3(
                        viewModel = viewModel,
                        onPost3Success = {
                            navController.navigate(NavRoutes.CreatePost4)
                        }
                    )
                }

                composable(NavRoutes.CreatePost4) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.CreatePostGraph)
                    }
                    val viewModel: CreatePostViewModel = hiltViewModel(parentEntry)
                    CreatePostScreen4(
                        viewModel = viewModel,
                        onPost4Success = {
                            navController.navigate(NavRoutes.MAINMENU) {
                                popUpTo(NavRoutes.CreatePostGraph) { inclusive = true }
                            }
                        }
                    )
                }
            }

            navigation(
                startDestination = NavRoutes.SEARCHPOST1,
                route = NavRoutes.SEARCHPOSTGRAPH
            ) {
                composable(NavRoutes.SEARCHPOST1) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.SEARCHPOSTGRAPH)
                    }
                    val viewModel: SearchPostViewModel = hiltViewModel(parentEntry)
                    val context = LocalContext.current
                    SearchPostScreen1(
                        viewModel = viewModel,
                        onSearchPost1Success = {
                            if (LocationUtils.hasLocationPermission(context)) {
                                navController.navigate(NavRoutes.SEARCHPOST2)
                            } else {
                                navController.navigate(NavRoutes.LocationPermission)
                            }
                        }
                    )
                }

                composable(NavRoutes.LocationPermission) {
                    LocationPermissionScreen(
                        onPermissionResult = { isGranted ->
                            navController.navigate(NavRoutes.SEARCHPOST2) {
                                //popUpTo(NavRoutes.SEARCHPOST1) { inclusive = true }
                            }
                        }
                    )
                }

                composable(NavRoutes.SEARCHPOST2) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.SEARCHPOSTGRAPH)
                    }
                    val viewModel: SearchPostViewModel = hiltViewModel(parentEntry)
                    SearchPostScreen2(
                        viewModel = viewModel,
                        onSearchPost2Success = {
                            navController.navigate(NavRoutes.SEARCHPOST3)
                        }
                    )
                }

                composable(NavRoutes.SEARCHPOST3) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.SEARCHPOSTGRAPH)
                    }
                    val viewModel: SearchPostViewModel = hiltViewModel(parentEntry)
                    SearchPostScreen3(
                        viewModel = viewModel,
                        onPostItemClick = { post ->
                            sharedViewModel.onSelectedPostChange(post)
                            navController.navigate(NavRoutes.POSTDETAIL)
                        },
                        onChangeLocationClick = {
                            navController.navigate(NavRoutes.SEARCHPOST2)
                        },
                        onChangeRangeClick = {
                            navController.navigate(NavRoutes.SEARCHPOST2)
                        },
                        onMainMenuClick = {
                            navController.navigate(NavRoutes.MAINMENU)
                        },
                        onDonateClick = {
                            navController.navigate(NavRoutes.CreatePostGraph)
                        },
                        onMessagesClick = {
                            navController.navigate(NavRoutes.MAINMENU)
                        }
                    )
                }
            }

            composable(NavRoutes.POSTDETAIL) {
                sharedViewModel.selectedPost.value?.let { it1 ->
                    PostDetailScreen(
                        post = it1
                    )
                }
            }

            navigation(
                startDestination = NavRoutes.USERPOSTS1,
                route = NavRoutes.USERPOSTSGRAPH
            ) {
                composable(NavRoutes.USERPOSTS1) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.USERPOSTSGRAPH)
                    }
                    val viewModel: UserPostsViewModel = hiltViewModel(parentEntry)
                    UserPostsScreen(
                        viewModel = viewModel,
                        onViewInterestedClick = { userPostUI ->
                            viewModel.onUserPostApplicantsSelectedChange(userPostUI)
                            navController.navigate(NavRoutes.USERPOSTS2)
                        },
                        onViewPostClick = { post ->
                            sharedViewModel.onSelectedPostChange(post)
                            navController.navigate(NavRoutes.POSTDETAIL)
                        }
                    )
                }

                composable(NavRoutes.USERPOSTS2) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(NavRoutes.USERPOSTSGRAPH)
                    }
                    val viewModel: UserPostsViewModel = hiltViewModel(parentEntry)
                    UserPostApplicantsScreen (
                        viewModel = viewModel,
                        onPostItemClick = { post ->
                            sharedViewModel.onSelectedPostChange(post)
                            navController.navigate(NavRoutes.POSTDETAIL)
                        },
                        userPost = viewModel.userPostApplicantsSelected.value,
                        onOpenChatClick = {
                            navController.navigate(NavRoutes.MAINMENU)
                        }
                    )
                }
            }

        }
    }
}


