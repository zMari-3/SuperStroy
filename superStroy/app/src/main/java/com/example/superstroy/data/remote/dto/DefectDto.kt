package com.example.superstroy.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefectDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val responsible: String,
    val term: String,
    @SerialName("created_date") val createdDate: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    val status: String
)
