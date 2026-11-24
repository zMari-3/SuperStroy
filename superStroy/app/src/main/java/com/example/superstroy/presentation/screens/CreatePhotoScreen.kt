package com.example.superstroy.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.superstroy.presentation.ui.componets.button.PrimaryButton
import com.example.superstroy.presentation.ui.componets.inputs.PhotoThumbnailRow
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens

@Composable
fun CreatePhotoScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "Создание дефекта", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.SpaceL)
        ) {
            // Главное фото (заглушка)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(Dimens.CornerM))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            // Миниатюры
            PhotoThumbnailRow(
                photos = listOf("1"), // Заглушка
                onAddClick = { /* Открыть галерею */ }
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            // Кнопки
            Button(
                onClick = { /* Открыть камеру */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(Dimens.CornerS)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(modifier = Modifier.width(Dimens.SpaceS))
                Text("Сделать фото")
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            OutlinedButton(
                onClick = { /* Открыть галерею */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(Dimens.CornerS)
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                Spacer(modifier = Modifier.width(Dimens.SpaceS))
                Text("Выбрать из галереи")
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            Text(
                text = "Фото помогает быстрее устранить дефект",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Продолжить",
                onClick = onContinueClick,
                modifier = Modifier.padding(bottom = Dimens.SpaceL)
            )
        }
    }
}
