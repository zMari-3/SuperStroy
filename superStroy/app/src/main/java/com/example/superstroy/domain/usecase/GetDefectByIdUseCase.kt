package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.repository.DefectRepository
import javax.inject.Inject

class GetDefectByIdUseCase @Inject constructor(
    private val repository: DefectRepository
) {
    suspend operator fun invoke(id: String): Result<Defect>
    {
        return try{
            val defect = repository.getDefectById(id)
            if (defect != null)
            {
                Result.success(defect)
            }
            else{
                Result.failure(Exception("Дефект не найден"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}