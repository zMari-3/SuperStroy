package com.example.superstroy.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSyncClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Считываем состояния из ViewModel
    val reminders by viewModel.remindersEnabled.collectAsState()
    val statusChanges by viewModel.statusChangesEnabled.collectAsState()
    val sync by viewModel.syncEnabled.collectAsState()
    val offlineMode by viewModel.offlineModeEnabled.collectAsState()
    val theme by viewModel.theme.collectAsState()

    Scaffold(
        topBar = { AppTopBar(title = "Настройки", onBackClick = onBackClick) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.SpaceL)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            Text("Уведомления",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            SettingCard {
                SettingSwitchRow(
                    title = "Напоминания о сроках",
                    subtitle = "Уведомлять о приближении срока",
                    isChecked = reminders,
                    onCheckedChange = { viewModel.toggleReminders(it) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingSwitchRow(
                    title = "Изменение статуса",
                    subtitle = "Когда дефект закрыт",
                    isChecked = statusChanges,
                    onCheckedChange = { viewModel.toggleStatusChanges(it) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingSwitchRow(
                    title = "Синхронизация",
                    subtitle = "Статус отправки записей",
                    isChecked = sync,
                    onCheckedChange = { viewModel.toggleSync(it) }
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceXL))

            Text("Приложение",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            SettingCard {
                SettingTextRow(
                    title = "Тема",
                    value = theme,
                    onClick = {
                        // Смена темы по клику
                        viewModel.changeTheme(if (theme == "Светлая") "Темная" else "Светлая")
                        Toast.makeText(
                            context,
                            "Тема изменена на: ${if (theme == "Светлая") "Темная" else "Светлая"}",
                            Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingTextRow(
                    title = "Язык",
                    value = "Русский",
                    onClick = {
                        Toast.makeText(context,
                        "Смена языка в разработке",
                        Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingTextRow(
                    title = "Офлайн-режим",
                    value = if (offlineMode) "Включён" else "Выключен",
                    valueColor = if (offlineMode)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = { viewModel.toggleOfflineMode(!offlineMode) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingTextRow(
                    title = "Очистить кэш",
                    value = "Удалить временные данные",
                    onClick = { Toast.makeText(context,
                        "Кэш очищен",
                        Toast.LENGTH_SHORT).show() }
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            SettingCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSyncClick() } //Переход на экран синхронизации
                        .padding(Dimens.SpaceM),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer,
                                CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(Dimens.SpaceM))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Синхронизация",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface)
                        Text("Последняя проверка сегодня",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Перейти",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceXL))
        }

    }
}

@Composable
fun SettingCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp))
    ) {
        content()
    }
}

@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun SettingTextRow(
    title: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceL),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface)
        Text(value, fontSize = 14.sp, color = valueColor)
    }
}
