package com.example.superstroy.data.remote.dto


import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DefectApi {
    @GET("rest/v1/defects")
    suspend fun getDefects(
        @Header("apikey") apiKey: String,
        @Header("Authorization") bearerToken: String,
        @Query("select") select: String = "*"
    ): List<DefectDto>
}
