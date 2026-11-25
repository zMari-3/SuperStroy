package com.example.superstroy.presentation.screens

import android.Manifest
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.superstroy.presentation.ui.componets.RequestPermissionWithRationale
import com.example.superstroy.presentation.ui.componets.button.SecondaryButton
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import kotlinx.coroutines.delay

@Composable
fun VoiceInputScreen(
    onBackClick: () -> Unit,
    onStopClick: (String) -> Unit
) {
    var seconds by remember { mutableIntStateOf(0) }

    // Таймер
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            seconds++
        }
    }

    // Анимация пульсации
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    var showPermissionDialog by remember { mutableStateOf(true) }
    if (showPermissionDialog) {
        RequestPermissionWithRationale(
            permission = Manifest.permission.RECORD_AUDIO,
            rationaleTitle = "Доступ к микрофону",
            rationaleMessage = "Нам нужен микрофон, чтобы вы могли надиктовать данные о дефекте голосом.",
            onPermissionGranted = {
                showPermissionDialog = false
                // Начинаем запись
            }
        )
    }

    Scaffold(
        topBar = { AppTopBar(
            title = "Голосовой ввод",
            onBackClick = onBackClick
        ) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.SpaceL),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Кнопка микрофона с анимацией
            Box(contentAlignment = Alignment.Center) {
                // Внешние круги
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(scale)
                        .background(
                            MaterialTheme
                                .colorScheme
                                .primaryContainer
                                .copy(alpha = 0.3f), CircleShape))
                Box(modifier = Modifier
                    .size(130.dp)
                    .background(
                        MaterialTheme
                            .colorScheme
                            .primaryContainer
                            .copy(alpha = 0.6f), CircleShape))
                // Центральный круг
                Box(
                    modifier = Modifier
                        .size(80.dp).
                        background(
                            MaterialTheme
                                .colorScheme.primary,
                            CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Микрофон",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceXXL))

            Text(
                text = "Говорите...",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            Text(
                text = "Опишите дефект голосом." +
                        "\nНапример: трещина в стене, Иванов," +
                        "\nсрок устранения — 20 мая",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка остановки
            SecondaryButton(
                text = "Остановить",
                onClick = {
                    // Имитация распознавания
                    onStopClick("Трещина в стене, Иванов, 20.05.2026")
                }
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Text(
                text = String.format(
                    LocalLocale.current.platformLocale,
                    "00:%02d",
                    seconds),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = Dimens.SpaceXL)
            )
        }
    }
}
