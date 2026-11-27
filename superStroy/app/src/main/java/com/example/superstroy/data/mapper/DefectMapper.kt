package com.example.superstroy.data.mapper

import com.example.superstroy.data.local.DefectEntity
import com.example.superstroy.data.remote.dto.DefectDto
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun DefectDto.toDomain() = Defect(
        id = id,
        title = title,
        description = description,
        responsible = responsible,
        term = try { LocalDate.parse(term)
        } catch (e: Exception) { LocalDate.now() },
        createdDate = try {
                LocalDateTime.parse(
                        createdDate,
                        DateTimeFormatter.ISO_DATE_TIME
                )
        } catch (e: Exception)
        { LocalDateTime.now()
        },
        latitude = latitude,
        longitude = longitude,
        photoUrl = photoUrl,
        status = try {
                DefectStatus.valueOf(status.uppercase())
        } catch (e: Exception) { DefectStatus.OPEN },
        isSynced = true
)

fun DefectEntity.toDomain() = Defect (
        id = id ?: "",
        title = title ?: "",
        description = description,
        responsible = responsible ?: "",
        term = term ?: LocalDate.now(),
        createdDate = createdDate ?: LocalDateTime.now(),
        latitude = latitude,
        longitude = longitude,
        photoUrl = photoUrl,
        status = status ?: DefectStatus.OPEN,
        isSynced = isSynced
)


fun Defect.toEntity(isSynced: Boolean = false) = DefectEntity (
        id = id,
        title = title,
        description = description,
        responsible = responsible,
        term = term,
        createdDate = createdDate,
        latitude = latitude,
        longitude = longitude,
        photoUrl = photoUrl,
        status = status,
        isSynced = isSynced
)

fun Defect.toDto() = DefectDto(
        id = id,
        title = title,
        description = description,
        responsible = responsible,
        term = term.format(DateTimeFormatter.ISO_LOCAL_DATE),
        createdDate = createdDate.format(DateTimeFormatter.ISO_DATE_TIME),
        latitude = latitude,
        longitude = longitude,
        photoUrl = photoUrl,
        status = status.name
)
