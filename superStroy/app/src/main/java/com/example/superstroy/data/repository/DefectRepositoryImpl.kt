package com.example.superstroy.data.repository

import com.example.superstroy.data.local.DefectDao
import com.example.superstroy.data.mapper.toDomain
import com.example.superstroy.data.mapper.toDto
import com.example.superstroy.data.mapper.toEntity
import com.example.superstroy.data.remote.DefectRemoteDataSource
import com.example.superstroy.data.worker.SyncScheduler
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
    private val syncScheduler: SyncScheduler,
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
        //Сохраняем локально
        val entity = defect.toEntity(isSynced = false)
        dao.insertDefect(entity)

        //Пытаемся отправить на сервер
        externalScope.launch(Dispatchers.IO) {
            try {
                remote.insertDefect(entity.toDomain().toDto())
                dao.updateDefect(entity.copy(isSynced = true))
            } catch (e: Exception) {
                // Если ошибка сети — WorkManager отправит позже
                syncScheduler.scheduleSync()
            }
        }
    }

    override suspend fun update(defect: Defect) {
        val entity = defect.toEntity(isSynced = false)
        dao.updateDefect(entity)
        externalScope.launch(Dispatchers.IO) {
            try {
                remote.updateDefect(defect.id, defect.toDto())
                dao.updateDefect(entity.copy(isSynced = true))
            } catch (e: Exception) {
                syncScheduler.scheduleSync()
            }
        }
    }

    override suspend fun delete(defect: Defect) {
        dao.deleteDefectById(defect.id)
        externalScope.launch(Dispatchers.IO) {
            try {
                remote.deleteDefect(defect.id)
                dao.deleteDefectById(defect.id)
            } catch (e: Exception) {
                // В реальности здесь нужно помечать на удаление (soft delete)
            }
        }

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
            val updatedEntity = existingEntity.copy(status = status, isSynced = false)
            dao.updateDefect(updatedEntity)

            // Пытаемся отправить на сервер
            externalScope.launch(Dispatchers.IO) {
                try {
                    remote.updateDefect(defectId, updatedEntity.toDomain().toDto())
                    dao.updateDefect(updatedEntity.copy(isSynced = true))
                } catch (e: Exception) {
                    syncScheduler.scheduleSync() // Если нет сети, отправим позже
                }
            }
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
                val entities = remoteDefects.map {
                    it.toDomain().toEntity(isSynced = true)
                }
                dao.insertDefects(entities)

            } catch (e: Exception) {
                // Если сети нет, ничего не делаем (данные останутся из кэша Room)
            }
        }
    }

    override suspend fun syncPendingDefects() {
        val unsynced = dao.getUnsyncedDefects()
        for (entity in unsynced) {
            try {
                remote.insertDefect(entity.toDomain().toDto())
                dao.updateDefect(entity.copy(isSynced = true))
            } catch (e: Exception) {
                // Если не удалось, оставляем в базе
            }
        }
    }

}
