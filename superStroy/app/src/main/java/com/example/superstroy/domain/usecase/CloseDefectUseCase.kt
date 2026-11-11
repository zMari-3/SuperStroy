package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.DefectRepository
import javax.inject.Inject

//Меняем статус на закрыт
class CloseDefectUseCase @Inject constructor(
    private val repository: DefectRepository
) {
    //Unit = void
    suspend operator fun invoke(defectId: String): Result<Unit>
    {
        return try {
            repository.changeStatus(defectId, DefectStatus.CLOSED)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}