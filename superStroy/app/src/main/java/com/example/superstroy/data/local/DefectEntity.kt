package com.example.superstroy.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "defects")
data class DefectEntity (
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val responsible: String,
    val term: LocalDate,
    @ColumnInfo(name = "created_date") val createdDate: LocalDateTime,
    val latitude: Double?,
    val longitude: Double?,
    @ColumnInfo(name = "photo_url") val photoUrl: String?,
    val status: DefectStatus,
    val isSynced: Boolean = false
)

