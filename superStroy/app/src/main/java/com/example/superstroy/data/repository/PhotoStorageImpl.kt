package com.example.superstroy.data.repository

import android.content.Context
import android.net.Uri
import com.example.superstroy.domain.repository.PhotoStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class PhotoStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
): PhotoStorage {
    override suspend fun savePhotoToLocal(uri: String): String = withContext(Dispatchers.IO) {
        // Возвращаем путь (или сохраняем копию)

        val sourceUri = Uri.parse(uri)
        val fileName = "defect_photo_${UUID.randomUUID()}.jpg"
        val destinationFile = File(context.filesDir, fileName)

        try {
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            destinationFile.absolutePath
        } catch (e: Exception) {
            // Если не удалось скопировать, возвращаем исходный путь
            uri
        }
    }
    override suspend fun savePhotoToCloud(path: String): String {
        // Реализовать загрузку в Supabase Storage (через Retrofit)
        return path
    }

}


