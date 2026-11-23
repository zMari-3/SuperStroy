package com.example.superstroy.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.usecase.CreateDefectUseCase
import com.example.superstroy.domain.usecase.ParseVoiceCommandUseCase
import com.example.superstroy.domain.usecase.ValidateDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NewDefectViewModel @Inject constructor(
    private val parseVoiceCommandUseCase: ParseVoiceCommandUseCase,
    private val validateDefect: ValidateDefectUseCase,
    private val createDefectUseCase: CreateDefectUseCase,
    savedStateHandle: SavedStateHandle // Получаем аргументы навигации
) : ViewModel() {


    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var responsible by mutableStateOf("")
    var term by mutableStateOf("")
    var geolocation by mutableStateOf("0.0, 0.0")
    var errorMessage by mutableStateOf<String?>(null)
    var isSaving by mutableStateOf(false)

    init {
        val voiceText = savedStateHandle.get<String>("voiceText") ?: ""
        if (voiceText.isNotBlank()) {
            val parsed = parseVoiceCommandUseCase(voiceText)
            if (parsed != null) {
                title = parsed.title
                responsible = parsed.responsible
                term = parsed.term
            }
        }
    }

    fun onTitleChange(newTitle: String) { title = newTitle; errorMessage = null }
    fun onDescriptionChange(newDesc: String) { description = newDesc }
    fun onResponsibleChange(newResp: String) { responsible = newResp; errorMessage = null }
    fun onTermChange(newTerm: String) { term = newTerm; errorMessage = null }

    fun saveDefect(onSuccess: () -> Unit) {
        //Валидация
        val validationResult = validateDefect(title, responsible, term)
        if (validationResult.isFailure) {
            errorMessage = validationResult.exceptionOrNull()?.message
            return
        }

        //Сохранение
        viewModelScope.launch {
            isSaving = true
            // Пытаемся распарсить дату
            val parsedDate = try {
                LocalDate.parse(term, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            } catch (e: Exception) {
                LocalDate.now()
            }

            // Предполагаем, что координаты фиксированные (заглушка)
            val result = createDefectUseCase(
                title = title,
                responsible = responsible,
                term = parsedDate,
                tempPhotoUri = null // Позже добавить сюда реальный Uri
            )

            isSaving = false
            if (result != null) {
                onSuccess()
            } else {
                errorMessage = "Не удалось сохранить дефект"
            }
        }
    }
}

