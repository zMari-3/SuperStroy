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
    suspend operator fun invoke(text: String): Result<ParsedCommand>
    {
        //Если строка пустая, вызываем исключение
        if(text.isBlank())
        {
            return Result.failure(IllegalArgumentException("Текст пуст"))
        }
        //Парсим строку
        return try {
            val parts = text.split(",").map { it.trim() }
            val title = parts.toString()
            val responsible = parts.toString()
            val term = parts.toString()

            Result.success(ParsedCommand(title, responsible, term))
        }catch (e: Exception){
            Result.failure(e)
        }

    }
}