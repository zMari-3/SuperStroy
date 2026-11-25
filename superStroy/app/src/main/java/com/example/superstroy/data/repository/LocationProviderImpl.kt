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
    override suspend fun getCurrentLocation(): GeoLocation? {
        return try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Проверяем, включен ли GPS вообще
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                return null // GPS выключен пользователем
            }

            // Пытаемся получить последнюю известную локацию (если есть разрешение)
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (location != null) {
                GeoLocation(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: SecurityException) {
            null
        } catch (e: SecurityException) {
            null
        }
    }
}
