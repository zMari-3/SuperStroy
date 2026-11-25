package com.example.superstroy.presentation.ui.componets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun RequestPermissionWithRationale(
    permission: String,
    rationaleTitle: String,
    rationaleMessage: String,
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    // Проверяем текущий статус разрешения
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    // Сохраняем, запрашивали ли мы уже разрешение
    var hasRequestedBefore by rememberSaveable { mutableStateOf(false) }

    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Лаунчер запроса разрешения
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            onPermissionGranted()
        } else {
            // Если отказали, проверяем, не стоит ли галочка "Больше не спрашивать"
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission)
            if (!shouldShowRationale && hasRequestedBefore) {
                // Пользователь нажал "Больше не спрашивать"
                showSettingsDialog = true
            }
        }
    }

    // Логика первого запуска
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            val shouldShowRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)

            if (shouldShowRationale) {
                // Система считает, что нужно объяснить (первый раз)
                showRationaleDialog = true
            } else if (!hasRequestedBefore) {
                // Запрашиваем сразу в первый раз
                hasRequestedBefore = true
                launcher.launch(permission)
            }
        } else {
            onPermissionGranted()
        }
    }

    // Диалог с объяснением (Rationale)
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text(text = rationaleTitle) },
            text = { Text(text = rationaleMessage) },
            confirmButton = {
                Button(onClick = {
                    showRationaleDialog = false
                    hasRequestedBefore = true
                    launcher.launch(permission)
                }) {
                    Text("Разрешить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    // Диалог перехода в настройки (если "Больше не спрашивать")
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Разрешение заблокировано") },
            text = { Text(
                "Вы отключили это разрешение. " +
                        "Пожалуйста, перейдите " +
                        "в настройки приложения и включите его вручную.") },
            confirmButton = {
                Button(onClick = {
                    showSettingsDialog = false
                    openAppSettings(context)
                }) {
                    Text("Открыть настройки")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

// Функция для открытия настроек приложения
private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package",
            context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
