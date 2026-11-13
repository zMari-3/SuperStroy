package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.repository.DefectRepository
import javax.inject.Inject

class GetDefectByIdUseCase @Inject constructor(
    private val repository: DefectRepository
) {
    suspend operator fun invoke(id: String): Any?
    {
        return try{
            repository.getDefectById(id)
        }catch (e: Exception){
            null
        }
    }
}