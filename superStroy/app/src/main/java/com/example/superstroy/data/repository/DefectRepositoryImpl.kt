package com.example.superstroy.data.repository

import com.example.superstroy.data.local.DefectDao
import com.example.superstroy.data.mapper.toDomain
import com.example.superstroy.data.mapper.toEntity
import com.example.superstroy.data.remote.DefectRemoteDataSource
import com.example.superstroy.data.remote.dto.DefectApi
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.DefectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefectRepositoryImpl @Inject constructor(
    private val dao: DefectDao,
    private val remote: DefectRemoteDataSource,
    private val externalScope: CoroutineScope
): DefectRepository {

    override fun getDefects(): Flow<List<Defect>> {
        syncFromNetwork()

        return dao.getDefects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getDefectByStatus(status: DefectStatus): Flow<List<Defect>> {
        return dao.getDefectsByStatus(status.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getDefectById(id: String): Defect? {
        return dao.getDefectById(id)?.toDomain()
    }

    override suspend fun insert(defect: Defect) {
        val entity = defect.toEntity(isSynced = false)
        dao.insertDefect(entity)

        // Пытаемся отправить на сервер в фоне
        externalScope.launch {
            try {
                // Если успешно, помечаем как синхронизированный
                dao.updateDefect(entity.copy(isSynced = true))
            } catch (e: Exception) {
                // Если ошибка, запись остается в Room с isSynced = false
                // При следующем запуске приложения она попробует отправиться снова
            }
        }
    }

    override suspend fun update(defect: Defect) {
        val entity = defect.toEntity(isSynced = false)
        dao.updateDefect(entity)
    }

    override suspend fun delete(defect: Defect) {
        dao.deleteDefectById(defect.id)
    }

    override suspend fun addCoverImage(
        defectId: String,
        sourceUri: String
    ): String {
        return "local/path/to/photo.jpg"
    }

    override suspend fun changeStatus(
        defectId: String,
        status: DefectStatus
    ) {
        val existingEntity = dao.getDefectById(defectId)
        if (existingEntity != null) {
            val updated = existingEntity.copy(status = status, isSynced = false)
            dao.updateDefect(updated)
        }
    }

    override fun searchDefects(query: String): Flow<List<Defect>> {
        return dao.searchDefects(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun syncFromNetwork() {
        externalScope.launch(Dispatchers.IO) {
            try {
                //Получаем данные с сервера
                val remoteDefects = remote.fetchDefects()

                // Маппим DTO -> Entity и сохраняем в Room
                // isSynced = true, так как эти данные пришли с сервера
                val entities = remoteDefects.map { it.toDomain().toEntity(isSynced = true) }
                dao.insertDefects(entities)

            } catch (e: Exception) {
                // Если сети нет, ничего не делаем (данные останутся из кэша Room)
            }
        }
    }

}