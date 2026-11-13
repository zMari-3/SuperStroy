package com.example.superstroy.domain.repository

import android.R.attr.delay
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

class FakeDefectRepository : DefectRepository {

    // Хранилище в памяти, обернутое в StateFlow для реактивности
    private val defectsFlow = MutableStateFlow<List<Defect>>(emptyList())

    // Флаги для имитации ошибок и задержек в тестах
    var shouldReturnError = false
    var delayMillis = 0L

    // Вспомогательный метод для имитации работы сети/БД
    private suspend fun simulateNetwork() {
        if (delayMillis > 0) delay(delayMillis)
        if (shouldReturnError) throw Exception("Имитация ошибки сети")
    }

    override fun getDefects(): Flow<List<Defect>> {
        return defectsFlow
    }

    override fun getDefectByStatus(status: DefectStatus): Flow<List<Defect>> {
        return defectsFlow.map { defects ->
            defects.filter { it.status == status }
        }
    }

    override fun getDefectById(id: String): Defect? {
        return defectsFlow.value.find { it.id == id }
    }

    override suspend fun insert(defect: Defect) {
        simulateNetwork()
        val newDefect = defect.copy(id = UUID.randomUUID().toString())
        defectsFlow.update { currentList -> currentList + newDefect }
    }

    override suspend fun update(defect: Defect) {
        simulateNetwork()
        defectsFlow.update { currentList ->
            currentList.map { if (it.id == defect.id) defect else it }
        }
    }

    override suspend fun delete(defect: Defect) {
        simulateNetwork()
        defectsFlow.update { currentList ->
            currentList.filterNot { it.id == defect.id }
        }
    }

    override suspend fun addCoverImage(defectId: String, sourceUri: String): String {
        simulateNetwork()
        return "fake_local_path_$defectId.jpg"
    }

    override suspend fun changeStatus(defectId: String, status: DefectStatus) {
        simulateNetwork()
        defectsFlow.update { currentList ->
            currentList.map {
                if (it.id == defectId) it.copy(status = status) else it
            }
        }
    }

    override fun searchDefects(query: String): Flow<List<Defect>> {
        return defectsFlow.map { defects ->
            defects.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description?.contains(query, ignoreCase = true) == true
            }
        }
    }


    fun setDefects(defects: List<Defect>) {
        defectsFlow.value = defects
    }
}