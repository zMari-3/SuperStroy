package com.example.superstroy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.superstroy.presentation.screens.CreatePhotoScreen
import com.example.superstroy.presentation.screens.MainScreen
import com.example.superstroy.presentation.screens.NewDefectScreen
import com.example.superstroy.presentation.screens.SplashScreen
import com.example.superstroy.presentation.screens.VoiceInputScreen


// Список маршрутов
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Main : Screen("main")
    data object CreatePhoto : Screen("create_photo")
    data object VoiceInput : Screen("voice_input")
    data object NewDefect : Screen("new_defect?voiceText={voiceText}") {
        fun createRoute(voiceText: String = "") = "new_defect?voiceText=$voiceText"
    }
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
                onDefectClick = { /* Навигация на детали */ },
                onCreateDefectClick = { navController.navigate(Screen.CreatePhoto.route) }
            )
        }

        composable(Screen.CreatePhoto.route) {
            CreatePhotoScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(Screen.VoiceInput.route) }
            )
        }

        composable(Screen.VoiceInput.route) {
            VoiceInputScreen(
                onBackClick = { navController.popBackStack() },
                onStopClick = { recognizedText ->
                    // Переходим на экран формы, передавая распознанный текст
                    navController.navigate(Screen.NewDefect.createRoute(recognizedText))
                }
            )
        }

        composable(
            route = Screen.NewDefect.route,
            arguments = listOf(navArgument("voiceText") { defaultValue = "" })
        ) {
            NewDefectScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}