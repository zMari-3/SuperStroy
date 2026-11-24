package com.example.superstroy.domain.usecase

import javax.inject.Inject

class ValidateDefectUseCase @Inject constructor() {
    operator fun invoke(title: String, responsible: String, term: String): Result<Unit> {
        return when {
            title.isBlank() -> Result.failure(IllegalArgumentException("Название не может быть пустым"))
            responsible.isBlank() -> Result.failure(IllegalArgumentException("Укажите ответственного"))
            term.isBlank() -> Result.failure(IllegalArgumentException("Укажите срок устранения"))
            else -> Result.success(Unit)
        }
    }
}
