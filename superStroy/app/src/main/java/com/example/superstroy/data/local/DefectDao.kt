package com.example.superstroy.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DefectDao {

    @Query("SELECT * FROM defects ORDER BY created_date DESC")
    fun getDefects(): Flow<List<DefectEntity>>

    @Query("SELECT * FROM defects WHERE status = :status ORDER BY created_date DESC")
    fun getDefectsByStatus(status: String): Flow<List<DefectEntity>>

    @Query("SELECT * FROM defects WHERE id = :id")
    suspend fun getDefectById(id: String): DefectEntity?

    @Query("SELECT * FROM defects WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchDefects(query: String): Flow<List<DefectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefect(defect: DefectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefects(defects: List<DefectEntity>) // Для сохранения списка с сервера

    @Update
    suspend fun updateDefect(defect: DefectEntity)

    @Query("DELETE FROM defects WHERE id = :id")
    suspend fun deleteDefectById(id: String)

    @Query("SELECT * FROM defects WHERE isSynced = 0")
    suspend fun getUnsyncedDefects(): List<DefectEntity>
}