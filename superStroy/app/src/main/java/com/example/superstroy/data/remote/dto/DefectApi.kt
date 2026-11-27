package com.example.superstroy.data.remote.dto

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface DefectApi {
    @GET("rest/v1/defects")
    suspend fun getDefects(
        @Header("apikey") apiKey: String,
        @Header("Authorization") bearerToken: String,
        @Query("select") select: String = "*"
    ): List<DefectDto>

    @POST("rest/v1/defects")
    suspend fun createDefect(
        @Header("apikey") apiKey: String,
        @Header("Authorization") bearerToken: String,
        @Header("Prefer") prefer: String = "return=representation",
        @Body defect: DefectDto
    ): List<DefectDto>

    @PATCH("rest/v1/defects")
    suspend fun updateDefect(
        @Header("apikey") apiKey: String,
        @Header("Authorization") bearerToken: String,
        @Query("id") id: String, // Передаем "eq.UUID"
        @Body defect: DefectDto
    ): List<DefectDto>

    @DELETE("rest/v1/defects")
    suspend fun deleteDefect(
        @Header("apikey") apiKey: String,
        @Header("Authorization") bearerToken: String,
        @Query("id") id: String
    )

    @Multipart
    @POST("functions/v1/recognize-speech")
    suspend fun recognizeSpeech(
        @Header("Authorization") bearerToken: String,
        @Part audio: MultipartBody.Part
    ): SpeechResponse
}
