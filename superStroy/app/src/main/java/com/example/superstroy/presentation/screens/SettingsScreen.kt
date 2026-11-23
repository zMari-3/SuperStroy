package com.example.superstroy.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSyncClick: () -> Unit
) {
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

            Text("Уведомления", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            SettingCard {
                SettingSwitchRow("Напоминания о сроках", "Уведомлять о приближении срока", true)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingSwitchRow("Изменение статуса", "Когда дефект закрыт", true)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingSwitchRow("Синхронизация", "Статус отправки записей", true)
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceXL))

            Text("Приложение", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            SettingCard {
                SettingTextRow("Тема", "Светлая")
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingTextRow("Язык", "Русский")
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingTextRow("Офлайн-режим", "Включён", valueColor = MaterialTheme.colorScheme.primary)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SettingTextRow("Очистить кэш", "Удалить временные данные")
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            // Кнопка перехода на экран Синхронизации
            SettingCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSyncClick() } // <--- Обработчик клика
                        .padding(Dimens.SpaceM),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Иконка в кружочке
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
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
                        Text("Синхронизация", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("Последняя проверка сегодня", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
    ) {
        content()
    }
}

@Composable
fun SettingSwitchRow(title: String, subtitle: String, initialChecked: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = initialChecked,
            onCheckedChange = { /* TODO: Сохранить настройку в DataStore */ },
            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun SettingTextRow(title: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceL),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(value, fontSize = 14.sp, color = valueColor)
    }
}

