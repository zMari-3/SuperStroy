package com.example.superstroy.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.superstroy.data.di.NetworkError
import com.example.superstroy.data.local.DefectDao
import com.example.superstroy.data.mapper.toDomain
import com.example.superstroy.data.mapper.toDto
import com.example.superstroy.data.remote.DefectRemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncDefectsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dao: DefectDao,
    private val remoteDataSource: DefectRemoteDataSource
) : CoroutineWorker(context, params) {

    @Suppress("ReturnCount")
    override suspend fun doWork(): Result {
        val unsynced = dao.getUnsyncedDefects()
        for (entity in unsynced) {
            try {
                // Пытаемся отправить на сервер
                remoteDataSource.insertDefect(entity.toDomain().toDto())
                // Если успешно, обновляем статус в Room
                dao.updateDefect(entity.copy(isSynced = true))
            } catch (e: NetworkError) { // Если ошибка сети
                return Result.retry()
            } catch (e: Exception) { // Если ошибка данных (например, 400)
                dao.updateDefect(entity
                    .copy(isSynced = true)) // Помечаем как синхронизированный, чтобы не зациклиться, или удаляем
                return Result.failure() // Останавливаем
            }
        }
        return Result.success()
    }
}
