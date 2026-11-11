package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.DefectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDefectsByStatusUseCase @Inject constructor(
    private val repository: DefectRepository
){
    operator fun invoke(status: DefectStatus): Flow<List<Defect>>
    {
        return repository.getDefectByStatus(status)
    }
}