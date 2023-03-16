package com.example.myapplicationg.ui.page

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplicationg.ui.page.home.MainPage
import com.example.myapplicationg.ui.page.home.MainPageIntent
import com.example.myapplicationg.ui.page.home.MainPageViewModel
import com.example.myapplicationg.ui.page.login.LoginPage
import com.example.myapplicationg.ui.page.login.LoginIntent
import com.example.myapplicationg.ui.page.login.LoginViewModel
import com.example.myapplicationg.ui.page.login.Register
import com.example.myapplicationg.ui.page.postdetail.PostDetail
import com.example.myapplicationg.ui.page.postdetail.PostDetailIntent
import com.example.myapplicationg.ui.page.postdetail.PostDetailViewModel
import com.example.myapplicationg.ui.page.profile.Profile
import com.example.myapplicationg.ui.page.profile.ProfileViewModel


sealed class Screen(val route: String) {
    object MainPage : Screen("mainPage")
    object Detail : Screen("detail")
    object Profile : Screen("screen")
    object Login : Screen("login")
    object Register : Screen("register")
}


@Composable
fun AppNavHost(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLoggedIn by mainViewModel.loginFlow.collectAsState()
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) Screen.MainPage.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.MainPage.route) {
            val viewModel = hiltViewModel<MainPageViewModel>()
            val state by viewModel.uiState.collectAsState()
            MainPage(
                state = state,
                onLikeChanged = { viewModel.sendIntent(MainPageIntent.OnLikeChange(it)) },
                openPost = { navController.navigate("${Screen.Detail.route}/$it") },
                showNoNetWorkBar = { viewModel.sendIntent(MainPageIntent.ShowSnackBar) },
                snackBarShown = { viewModel.sendIntent(MainPageIntent.SnackBarShown) },
                publish = { viewModel.sendIntent(MainPageIntent.PublishPost(it)) }
            )
        }
        composable(
            "${Screen.Detail.route}/{postId}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType }
            )
        ) {
            val viewModel = hiltViewModel<PostDetailViewModel>()
            val state by viewModel.uiState.collectAsState()

            PostDetail(
                state = state,
                sentSnackBar = { viewModel.sendIntent(PostDetailIntent.SnackBarShown)},
                onBack = { navController.popBackStack() },
                onLikeChanged = { viewModel.sendIntent(PostDetailIntent.OnLikeChange(it)) },
                onSendComment = { viewModel.sendIntent(PostDetailIntent.PublishComment(it)) },
                onNoNetWork = {viewModel.sendIntent(PostDetailIntent.ShowNoNetworkSnackBar)}
            )
        }
        composable(
            "${Screen.Profile.route}/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            val state by viewModel.uiState.collectAsState()
            Profile(
                state = state,
                onNoNetwork = {  }
            )
        }
        composable(Screen.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            val state by viewModel.uiState.collectAsState()
            LoginPage(
                state = state,
                onSubmit = { name, password ->
                    viewModel.sendIntent(LoginIntent.Login(name, password))
                },
                onLoggedIn = { navController.navigate(Screen.MainPage.route) },
                onRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            val state by viewModel.uiState.collectAsState()
            Register(
                state = state,
                onSubmit = { viewModel.sendIntent(LoginIntent.Register(it)) },
                onBack = { navController.popBackStack() },
                onLoggedIn = { navController.navigate(Screen.MainPage.route) }
            )
        }

    }

}
