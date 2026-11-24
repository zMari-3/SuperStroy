package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.repository.DefectRepository
import javax.inject.Inject

class DeleteDefectUseCase @Inject constructor(
    private val repository: DefectRepository
) {
    suspend operator fun invoke(defect: Defect): Boolean {
        return try {
            repository.delete(defect)
            true
        } catch (e: Exception) {
            false
        }
    }
}
