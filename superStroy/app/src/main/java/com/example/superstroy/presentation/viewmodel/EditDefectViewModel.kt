package com.example.superstroy.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.usecase.GetDefectByIdUseCase
import com.example.superstroy.domain.usecase.UpdateDefectUseCase
import com.example.superstroy.domain.usecase.ValidateDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditDefectViewModel @Inject constructor(
    private val getDefectByIdUseCase: GetDefectByIdUseCase,
    private val updateDefectUseCase: UpdateDefectUseCase,
    private val validateDefectUseCase: ValidateDefectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val defectId: String = savedStateHandle["defectId"] ?: ""

    // Исходный дефект, чтобы не потерять данные
    private var originalDefect: Defect? = null

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var responsible by mutableStateOf("")
    var term by mutableStateOf("")
    var geolocation by mutableStateOf("")

    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    init {
        loadDefect()
    }

    private fun loadDefect() {
        viewModelScope.launch {
            val defect = getDefectByIdUseCase(defectId)
            if (defect != null) {
                originalDefect = defect
                title = defect.title
                description = defect.description ?: ""
                responsible = defect.responsible
                term = defect.term.format(dateFormatter)
                geolocation = "${defect.latitude ?: 0.0}, ${defect.longitude ?: 0.0}"
            } else {
                errorMessage = "Дефект не найден"
            }
            isLoading = false
        }
    }

    fun onTitleChange(newTitle: String) { title = newTitle; errorMessage = null }
    fun onDescriptionChange(newDesc: String) { description = newDesc }
    fun onResponsibleChange(newResp: String) { responsible = newResp; errorMessage = null }
    fun onTermChange(newTerm: String) { term = newTerm; errorMessage = null }

    fun saveChanges(onSuccess: () -> Unit) {
        val validationResult = validateDefectUseCase(title, responsible, term)
        if (validationResult.isFailure) {
            errorMessage = validationResult.exceptionOrNull()?.message
            return
        }

        val currentOriginal = originalDefect ?: return

        viewModelScope.launch {
            isLoading = true

            val parsedDate = try {
                LocalDate.parse(term, dateFormatter)
            } catch (e: Exception) {
                currentOriginal.term // Оставляем старую дату, если ввели неверно
            }

            // Создаем обновленный объект, копируя старые данные и заменяя новые
            val updatedDefect = currentOriginal.copy(
                title = title,
                description = description.ifBlank { null },
                responsible = responsible,
                term = parsedDate
            )

            val isSuccess = updateDefectUseCase(updatedDefect)
            isLoading = false

            if (isSuccess) {
                onSuccess()
            } else {
                errorMessage = "Не удалось сохранить изменения"
            }
        }
    }
}