package com.example.superstroy.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.data.repository.DefectRepositoryImpl
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.usecase.CreateDefectUseCase
import com.example.superstroy.domain.usecase.DeleteDefectUseCase
import com.example.superstroy.domain.usecase.GetDefectsUseCase
import com.example.superstroy.domain.usecase.SearchDefectsUseCase
import com.example.superstroy.domain.usecase.UpdateDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DefectViewModel @Inject constructor(
    private val getDefectsUseCase: GetDefectsUseCase,
    private val searchDefectsUseCase: SearchDefectsUseCase,
    private val updateDefectUseCase: UpdateDefectUseCase,
    private val deleteDefectUseCase: DeleteDefectUseCase,
    private val createDefectUseCase: CreateDefectUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(DefectUiState())
    val uiState: StateFlow<DefectUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        loadDefects()
    }

    fun loadDefects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Подписываемся на поиск или на получение всех дефектов
            searchQuery.flatMapLatest { query ->
                if (query.isBlank()) getDefectsUseCase() else searchDefectsUseCase(query)
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Ошибка загрузки") }
            }.collect { defects ->
                _uiState.update {
                    it.copy(
                        defects = defects,
                        isLoading = false,
                        emptyMessage = if (defects.isEmpty()) "Дефектов пока нет" else null
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun insertDefect(
        title: String, description: String?, responsible: String, term: LocalDate,
        latitude: Double?, longitude: Double?, photoUrl: String?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = createDefectUseCase(title, responsible, term, photoUrl)
            if (result != null) {
                loadDefects() // Перезагружаем список
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Не удалось создать дефект") }
            }
        }
    }

    fun updateDefect(defect: Defect) {
        viewModelScope.launch {
            val isSuccess = updateDefectUseCase(defect)
            if (isSuccess) loadDefects()
        }
    }

    fun deleteDefect(defect: Defect) {
        viewModelScope.launch {
            val isSuccess = deleteDefectUseCase(defect)
            if (isSuccess) loadDefects()
        }
    }

}