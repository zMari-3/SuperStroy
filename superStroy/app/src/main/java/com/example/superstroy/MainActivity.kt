package com.example.superstroy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.superstroy.presentation.navigation.AppNavigation
import com.example.superstroy.presentation.ui.theme.SuperStroyTheme
import com.example.superstroy.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Подключаем ViewModel для чтения темы
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themeString by settingsViewModel.theme.collectAsState()

            // Определяем, какая тема должна быть включена
            val darkTheme = when (themeString) {
                "Темная" -> true
                "Светлая" -> false
                else -> isSystemInDarkTheme() // "Системная"
            }

            SuperStroyTheme(
                darkTheme = darkTheme,
                dynamicColor = false // Отключаем Material You, чтобы цвета были как в Figma
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperStroyTheme {
        Greeting("Android")
    }
}
