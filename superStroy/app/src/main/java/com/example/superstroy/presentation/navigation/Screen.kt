package com.example.superstroy.presentation.navigation

import android.net.Uri

// Список маршрутов
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Main : Screen("main")
    data object CreatePhoto : Screen("create_photo")
    data object VoiceInput : Screen("voice_input?photoUri={photoUri}") {
        fun createRoute(photoUri: String) = "voice_input?photoUri=${Uri.encode(photoUri)}"
    }

    // Маршрут для Нового дефекта принимает и текст, и фото
    data object NewDefect : Screen("new_defect?voiceText={voiceText}&photoUri={photoUri}") {
        fun createRoute(voiceText: String = "", photoUri: String = "") =
            "new_defect?voiceText=${Uri.encode(voiceText)}&photoUri=${Uri.encode(photoUri)}"
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
