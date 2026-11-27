package com.example.superstroy.presentation.screens

import android.Manifest
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.superstroy.presentation.ui.componets.RequestPermissionWithRationale
import com.example.superstroy.presentation.ui.componets.button.SecondaryButton
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.viewmodel.VoiceInputViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalLocale

@Suppress("CyclomaticComplexMethod")
@Composable
fun VoiceInputScreen(
    onBackClick: () -> Unit,
    onStopClick: (String) -> Unit,
    viewModel: VoiceInputViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var seconds by remember { mutableIntStateOf(0) }
    var hasPermission by remember { mutableStateOf(false) }

    // Таймер
    LaunchedEffect(uiState) {
        if (uiState is VoiceInputViewModel.VoiceState.Recording) {
            seconds = 0
            while (true) {
                delay(1000L)
                seconds++
            }
        }
    }

    // Обработка результата
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is VoiceInputViewModel.VoiceState.Success -> {
                onStopClick(state.text) // Переходим на форму с текстом
                viewModel.resetState()
            }
            is VoiceInputViewModel.VoiceState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // Запрос разрешения перед записью
    if (!hasPermission) {
        RequestPermissionWithRationale(
            permission = Manifest.permission.RECORD_AUDIO,
            rationaleTitle = "Доступ к микрофону",
            rationaleMessage = "Нам нужен микрофон, чтобы вы могли надиктовать данные о дефекте голосом.",
            onPermissionGranted = {
                hasPermission = true
                viewModel.startRecording()
            }
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "Голосовой ввод", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.SpaceL),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(contentAlignment = Alignment.Center) {
                if (uiState is VoiceInputViewModel.VoiceState.Recording) {
                    // Анимация пульсации только во время записи
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
                    Box(modifier = Modifier
                        .size(180.dp)
                        .scale(scale)
                        .background(MaterialTheme.colorScheme
                            .primaryContainer.copy(alpha = 0.3f),
                            CircleShape))
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            if (uiState is VoiceInputViewModel.VoiceState.Recording)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (uiState is VoiceInputViewModel.VoiceState.Recording)
                            Icons.Default.Stop
                        else Icons.Default.Mic,
                        contentDescription = "Микрофон",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceXXL))

            Text(
                text = when (uiState) {
                    is VoiceInputViewModel.VoiceState.Recording -> "Идет запись..."
                    is VoiceInputViewModel.VoiceState.Processing -> "Распознаем речь..."
                    else -> "Говорите..."
                },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            Text(
                text = "Опишите дефект голосом.\nНапример: трещина в стене, Иванов," +
                        "\nсрок устранения — 20 мая",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            if (uiState is VoiceInputViewModel.VoiceState.Processing) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else if (uiState is VoiceInputViewModel.VoiceState.Recording) {
                SecondaryButton(
                    text = "Остановить",
                    onClick = { viewModel.stopRecordingAndProcess() }
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            // Таймер
            if (uiState is VoiceInputViewModel.VoiceState.Recording) {
                Text(
                    text = String.format(LocalLocale.current.platformLocale,
                        "00:%02d", seconds),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = Dimens.SpaceXL)
                )
            } else {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}
