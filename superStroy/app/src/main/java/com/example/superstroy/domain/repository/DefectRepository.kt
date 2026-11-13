package com.example.superstroy.domain.repository

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import kotlinx.coroutines.flow.Flow

interface DefectRepository {

    fun getDefects(): Flow<List<Defect>>

    fun getDefectByStatus(status: DefectStatus): Flow<List<Defect>>

    fun getDefectById(id: String): Defect?
    suspend fun insert(defect: Defect)

    suspend fun update(defect: Defect)

    suspend fun delete(defect: Defect)

    suspend fun addCoverImage(defectId: String, sourceUri: String): String
    suspend fun changeStatus(defectId: String, status: DefectStatus)
    fun searchDefects(query: String): Flow<List<Defect>>
}