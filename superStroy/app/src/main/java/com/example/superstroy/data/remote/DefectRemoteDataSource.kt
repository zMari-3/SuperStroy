package com.example.superstroy.data.remote

import com.example.superstroy.data.di.NetworkModule.API_KEY
import com.example.superstroy.data.di.toNetworkError
import com.example.superstroy.data.remote.dto.DefectApi
import com.example.superstroy.data.remote.dto.DefectDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class DefectRemoteDataSource @Inject constructor(
    private val api: DefectApi
) {
    private val authHeader = "Bearer $API_KEY"

    suspend fun fetchDefects(): List<DefectDto> {
        return try {
            api.getDefects(API_KEY, authHeader)
        } catch (e: Exception) {
            throw e.toNetworkError()
        }
    }

    suspend fun insertDefect(defect: DefectDto): DefectDto {
        return try {
            api.createDefect(API_KEY, authHeader, defect = defect).first()
        } catch (e: Exception) {
            throw e.toNetworkError()
        }
    }

    suspend fun updateDefect(id: String, defect: DefectDto) {
        try {
            api.updateDefect(API_KEY, authHeader, id = "eq.$id", defect = defect)
        } catch (e: Exception) {
            throw e.toNetworkError()
        }
    }

    suspend fun deleteDefect(id: String) {
        try {
            api.deleteDefect(API_KEY, authHeader, id = "eq.$id")
        } catch (e: Exception) {
            throw e.toNetworkError()
        }
    }

    suspend fun recognizeSpeech(file: File): String {
        return try {
            val requestBody = file.asRequestBody("audio/m4a".toMediaType())
            val part = MultipartBody.Part.createFormData("audio", file.name, requestBody)
            val response = api.recognizeSpeech(authHeader, part)
            response.text
        } catch (e: Exception) {
            throw e.toNetworkError()
        }
    }
}
