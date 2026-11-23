package com.example.superstroy.domain.usecase

import javax.inject.Inject

class ValidateDefectUseCase @Inject constructor() {
    operator fun invoke(title: String, responsible: String, term: String): Result<Unit> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Название не может быть пустым"))
        }
        if (responsible.isBlank()) {
            return Result.failure(IllegalArgumentException("Укажите ответственного"))
        }
        if (term.isBlank()) {
            return Result.failure(IllegalArgumentException("Укажите срок устранения"))
        }
        return Result.success(Unit)
    }
}