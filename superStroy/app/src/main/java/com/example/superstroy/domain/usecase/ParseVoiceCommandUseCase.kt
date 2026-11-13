package com.example.superstroy.domain.usecase

import java.time.LocalDate
import javax.inject.Inject

data class ParsedCommand(
    val title: String,
    val responsible: String,
    val term: String
)

class ParseVoiceCommandUseCase @Inject constructor()
{
    operator fun invoke(text: String): ParsedCommand?
    {
        //Парсим строку
        return try {
            val parts = text.split(",").map { it.trim() }

            val title = parts.getOrNull(0) ?: ""
            val responsible = parts.getOrNull(1) ?: ""
            val term = parts.getOrNull(2) ?: ""

            ParsedCommand(title, responsible, term)
        } catch (e: Exception) {
            null
        }

    }
}