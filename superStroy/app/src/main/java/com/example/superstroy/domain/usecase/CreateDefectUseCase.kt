package com.example.superstroy.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.DefectRepository
import com.example.superstroy.domain.repository.LocationProvider
import com.example.superstroy.domain.repository.PhotoStorage
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

//Обьеденяем все данные и создаем дефект
class CreateDefectUseCase @Inject constructor(
    private val repository: DefectRepository,
    private val locationProvider: LocationProvider,
    private val photoStorage: PhotoStorage
)
{
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        title: String,
        responsible: String,
        term: LocalDate,
        tempPhotoUri: String?
    ): Result<Defect>
    {
        return try {
            val localPhotoPath = tempPhotoUri?.let {
                photoStorage.savePhotoToLocal(it)
            } ?: ""
            val location = locationProvider.getCurrentLocation()
            val defect = Defect(
                id = UUID.randomUUID().toString(),
                title = title,
                description = "",
                responsible = responsible,
                term = term,
                created_date = LocalDateTime.now(),
                latitude = location?.latitude,
                longitude = location?.longitude,
                photo_url = localPhotoPath,
                status = DefectStatus.OPEN
            )
            repository.insert(defect)
        }catch (e: Exception){
            Result.failure<Unit>(e)
        } as Result<Defect>
    }
}