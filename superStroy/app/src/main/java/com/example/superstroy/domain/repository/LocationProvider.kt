package com.example.superstroy.domain.repository

data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
)

//Получаем координаты
interface LocationProvider {
    suspend fun getCurrentLocation(): GeoLocation
}