package com.example.superstroy.data.remote

import com.example.superstroy.data.di.NetworkModule.API_KEY
import com.example.superstroy.data.remote.dto.DefectApi
import com.example.superstroy.data.remote.dto.DefectDto
import javax.inject.Inject

class DefectRemoteDataSource @Inject constructor(
    private val api: DefectApi
) {
    suspend fun fetchDefects(): List<DefectDto> = api.getDefects(API_KEY, "Bearer $API_KEY", "*")
}
