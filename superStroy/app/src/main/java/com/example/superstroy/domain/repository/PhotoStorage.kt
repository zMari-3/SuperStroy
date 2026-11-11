package com.example.superstroy.domain.repository

interface PhotoStorage {
    //Сохраняем фото в базу
    suspend fun savePhotoToLocal(uri: String): String

    //Сохраняем фото в облако
    suspend fun savePhotoToCloud(path: String): String
}