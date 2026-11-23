package com.example.superstroy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.superstroy.presentation.screens.CloseDefectScreen
import com.example.superstroy.presentation.screens.CreatePhotoScreen
import com.example.superstroy.presentation.screens.DefectDetailsScreen
import com.example.superstroy.presentation.screens.EditDefectScreen
import com.example.superstroy.presentation.screens.MainScreen
import com.example.superstroy.presentation.screens.NewDefectScreen
import com.example.superstroy.presentation.screens.SettingsScreen
import com.example.superstroy.presentation.screens.SplashScreen
import com.example.superstroy.presentation.screens.SyncScreen
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
    data object DefectDetails : Screen("details/{defectId}") {
        fun createRoute(defectId: String) = "details/$defectId"
    }
    data object EditDefect : Screen("edit/{defectId}") {
        fun createRoute(defectId: String) = "edit/$defectId"
    }
    data object CloseDefect : Screen("close/{defectId}") {
        fun createRoute(defectId: String) = "close/$defectId"
    }
    data object Sync : Screen("sync")
    data object Settings : Screen("settings")
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
                onDefectClick = { id -> navController.navigate(Screen.DefectDetails.createRoute(id)) },
                onCreateDefectClick = { navController.navigate(Screen.CreatePhoto.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
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

        composable(Screen.DefectDetails.route) {
            DefectDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.EditDefect.createRoute(id)) },
                onCloseClick = { id -> navController.navigate(Screen.CloseDefect.createRoute(id)) }
            )
        }

        composable(Screen.CloseDefect.route) {
            CloseDefectScreen(
                onBackClick = { navController.popBackStack() },
                onCloseSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Sync.route) {
            SyncScreen(
                onBackClick = { navController.popBackStack() },
                onSyncClick = { /* Логика синхронизации */ }
            )
        }

        composable(
            route = Screen.EditDefect.route,
            arguments = listOf(navArgument("defectId") { type = NavType.StringType })
        ) {
            EditDefectScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = {
                    // После сохранения возвращаемся на экран деталей
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onSyncClick = { navController.navigate(Screen.Sync.route) }
            )
        }
    }
}