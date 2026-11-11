package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.repository.DefectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDefectsUseCase@Inject constructor(
    private val repository: DefectRepository
) {
    operator fun invoke(): Flow<List<Defect>>
    {
        return repository.getDefects()
    }
}