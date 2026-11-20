package com.example.superstroy.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.example.superstroy.domain.repository.GeoLocation
import com.example.superstroy.domain.repository.LocationProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationProviderImpl @Inject constructor (
    @ApplicationContext private val context: Context
): LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): GeoLocation {
        return try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // Пытаемся получить последнюю известную локацию (если есть разрешение)
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (location != null) {
                GeoLocation(location.latitude, location.longitude)
            } else {
                // Заглушка, если координат нет
                GeoLocation(0.0, 0.0)
            }
        } catch (e: SecurityException) {
            // Если нет разрешения, возвращаем заглушку
            GeoLocation(0.0, 0.0)
        }
    }
}
