package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.repository.DefectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.temporal.TemporalQuery
import javax.inject.Inject

class SearchDefectsUseCase @Inject constructor(
    private val repository: DefectRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Defect>>
    {
        //Обрезаем всё лишнее в строке
        val tQuery = query.trim()
        //Если строка пустая, возвращаем пустой список
        if(tQuery.isBlank()){
            return flowOf(emptyList())
        }
        return repository.searchDefects(tQuery)
    }
}