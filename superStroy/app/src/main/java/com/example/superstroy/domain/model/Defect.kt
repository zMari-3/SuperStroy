package com.example.superstroy.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

data class Defect @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String,
    val title: String,
    val description: String?,
    val responsible: String,
    val term: LocalDate,
    val createdDate: LocalDateTime,
    val latitude: Double?,
    val longitude: Double?,
    val photoUrl: String?,
    val status: DefectStatus
)

enum class DefectStatus{
    OPEN,
    CLOSED
}
