package com.example.superstroy.data.di

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException

sealed class NetworkError : Exception() {
    data object NoConnection : NetworkError()
    data object Timeout : NetworkError()
    data class ClientError(val code: Int, override val message: String?) : NetworkError()
    data class ServerError(val code: Int, override val message: String?) : NetworkError()
    data class Unknown(override val cause: Throwable) : NetworkError()
}

// Функция-расширение для преобразования любого исключения в NetworkError
fun Throwable.toNetworkError(): NetworkError = when (this) {
    is UnknownHostException -> NetworkError.NoConnection
    is SocketTimeoutException -> NetworkError.Timeout
    is HttpException -> {
        val code = code()
        val errorBody = response()?.errorBody()?.string()
        if (code in 400..499) NetworkError.ClientError(code, errorBody)
        else NetworkError.ServerError(code, errorBody)
    }
    is IOException -> NetworkError.NoConnection // Другие проблемы с сетью
    else -> NetworkError.Unknown(this)
}