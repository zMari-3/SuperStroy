package com.example.superstroy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.superstroy.presentation.screens.MainScreen
import com.example.superstroy.presentation.screens.SplashScreen


// Список маршрутов
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Main : Screen("main")
    data object Create : Screen("create")
}

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onStartClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true } //Удаляем Splash из стека
                    }
                },
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onDefectClick = { defectId ->
                    // navController.navigate(Screen.Details.createRoute(defectId))
                },
                onCreateDefectClick = {
                    navController.navigate(Screen.Create.route)
                }
            )
        }

    }
}