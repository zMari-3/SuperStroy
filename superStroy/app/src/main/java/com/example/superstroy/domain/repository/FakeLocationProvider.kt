package com.example.superstroy.domain.repository

class FakeLocationProvider : LocationProvider {
    override suspend fun getCurrentLocation(): GeoLocation {
        return GeoLocation(latitude = 55.7558, longitude = 37.6173) // Москва
    }
}
