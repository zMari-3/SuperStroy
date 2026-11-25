package com.example.superstroy.presentation.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.superstroy.presentation.ui.componets.RequestPermissionWithRationale
import com.example.superstroy.presentation.ui.componets.button.PrimaryButton
import com.example.superstroy.presentation.ui.componets.inputs.PhotoThumbnailRow
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import java.io.File

@Composable
fun CreatePhotoScreen(
    onBackClick: () -> Unit,
    onContinueClick: (List<String>) -> Unit
) {
    val context = LocalContext.current

    // Стейт для хранения выбранных/сделанных фото (храним строковые URI)
    var photoUris by rememberSaveable { mutableStateOf(arrayListOf<String>()) }

    // Стейт для временного URI камеры (куда сохранится фото)
    var tempCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    var showPermissionDialog by remember { mutableStateOf(false) }

    //Лаунчер для КАМЕРЫ
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            val newList = ArrayList(photoUris)
            newList.add(tempCameraUri.toString())
            photoUris = newList
        }
    }

    //Лаунчер для ГАЛЕРЕИ (Photo Picker - современный стандарт)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val newList = ArrayList(photoUris)
            newList.add(uri.toString())
            photoUris = newList
        }
    }

    if (showPermissionDialog) {
        RequestPermissionWithRationale(
            permission = Manifest.permission.CAMERA,
            rationaleTitle = "Доступ к камере",
            rationaleMessage = "Нам нужен доступ к камере, чтобы вы могли сфотографировать дефект на объекте.",
            onPermissionGranted = {
                showPermissionDialog = false
                // Создаем пустой файл и запускаем камеру
                val uri = createTempImageUri(context)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            }
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "Создание дефекта", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.SpaceL)
        ) {
            //Главное фото (превью последнего фото)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(Dimens.CornerM))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (photoUris.isNotEmpty()) {
                    AsyncImage(
                        model = photoUris.last(),
                        contentDescription = "Главное фото",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Фото пока нет", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            //  Миниатюры
            PhotoThumbnailRow(
                photos = photoUris,
                onAddClick = {
                    // При клике на плюс открываем галерею
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Button(
                onClick = { showPermissionDialog = true }, // Сначала запрашиваем разрешение
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(Dimens.CornerS)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(modifier = Modifier.width(Dimens.SpaceS))
                Text("Сделать фото")
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            OutlinedButton(
                onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
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
                onClick = { onContinueClick(photoUris) }, // Передаем список фото дальше
                modifier = Modifier.padding(bottom = Dimens.SpaceL),
                enabled = photoUris.isNotEmpty() // Нельзя продолжить без фото
            )
        }
    }
}

//  функция для создания временного файла
fun createTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "defect_photo_",
        ".jpg",
        context.cacheDir // Храним во временной папке кэша
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}
