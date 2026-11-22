package com.example.superstroy.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.superstroy.domain.usecase.ParseVoiceCommandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewDefectViewModel @Inject constructor(
    private val parseVoiceCommandUseCase: ParseVoiceCommandUseCase,
    savedStateHandle: SavedStateHandle // Получаем аргументы навигации
) : ViewModel() {

    // Состояния полей формы
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var responsible by mutableStateOf("")
        private set
    var term by mutableStateOf("")
        private set
    var geolocation by mutableStateOf("0.0, 0.0") // Заглушка для координат
        private set

    init {
        // Получаем текст, переданный с экрана голосового ввода
        val voiceText = savedStateHandle.get<String>("voiceText") ?: ""
        if (voiceText.isNotBlank()) {
            parseVoiceInput(voiceText)
        }
    }

    private fun parseVoiceInput(text: String) {
        // Используем наш Use Case
        val parsedResult = parseVoiceCommandUseCase(text)

        if (parsedResult != null) {
            title = parsedResult.title
            responsible = parsedResult.responsible
            term = parsedResult.term
        }
    }

    // Методы для обновления полей, если пользователь вводит текст вручную
    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }

    fun onResponsibleChange(newResponsible: String) {
        responsible = newResponsible
    }

    fun onTermChange(newTerm: String) {
        term = newTerm
    }

    fun saveDefect() {
        // Здесь в будущем будет вызов CreateDefectUseCase(title, responsible, ...)
    }
}