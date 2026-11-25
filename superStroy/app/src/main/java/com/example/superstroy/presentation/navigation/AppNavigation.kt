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
                onContinueClick = { photoUris ->
                    // Берем первое фото и передаем на экран голоса
                    val firstPhotoUri = photoUris.firstOrNull() ?: ""
                    navController.navigate(Screen.VoiceInput.createRoute(firstPhotoUri))
                }
            )
        }

        composable(
            route = Screen.VoiceInput.route,
            arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoUri = backStackEntry.arguments?.getString("photoUri") ?: ""
            VoiceInputScreen(
                onBackClick = { navController.popBackStack() },
                onStopClick = { recognizedText ->
                    // Передаем и текст, и фото дальше
                    navController.navigate(Screen.NewDefect.createRoute(recognizedText, photoUri))
                }
            )
        }

        composable(
            route = Screen.NewDefect.route,
            arguments = listOf(
                navArgument("voiceText") { defaultValue = "" },
                navArgument("photoUri") { defaultValue = "" }
            )
        ) { backStackEntry ->
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
