package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.DefectRepository
import javax.inject.Inject

//Меняем статус на закрыт
class CloseDefectUseCase @Inject constructor(
    private val repository: DefectRepository
) {
    suspend operator fun invoke(defectId: String): Boolean
    {
        return try {
            repository.changeStatus(defectId, DefectStatus.CLOSED)
            true
        } catch (e: Exception) {
            false
        }
    }
}