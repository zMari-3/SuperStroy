package com.example.superstroy.presentation.mapper

import com.example.superstroy.data.di.NetworkError

fun NetworkError.toUserMessage(): String {
    return when (this) {
        is NetworkError.NoConnection -> "Нет подключения к интернету. Проверьте сеть и попробуйте снова."
        is NetworkError.Timeout -> "Сервер не отвечает. Попробуйте позже."
        is NetworkError.ServerError -> "Ошибка на сервере (${code}). Мы уже работаем над этим."
        is NetworkError.ClientError -> "Ошибка запроса (${code}). Проверьте введенные данные."
        is NetworkError.Unknown -> "Произошла неизвестная ошибка. Попробуйте еще раз."
    }
}
