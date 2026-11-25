package com.example.superstroy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.usecase.GetDefectsUseCase
import com.example.superstroy.domain.usecase.SearchDefectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefectViewModel @Inject constructor(
    private val getDefectsUseCase: GetDefectsUseCase,
    private val searchDefectsUseCase: SearchDefectsUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(DefectUiState())
    val uiState: StateFlow<DefectUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private val selectedFilter = MutableStateFlow("Все")

    init {
        observeDefects()
    }

    private fun observeDefects() {
        viewModelScope.launch {
            combine(searchQuery, selectedFilter) { query, filter ->
                Pair(query, filter)
            }.flatMapLatest { (query, filter) ->
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                // Выбираем источник данных: поиск или все дефекты
                val sourceFlow = if (query.isBlank()) {
                    getDefectsUseCase()
                } else {
                    searchDefectsUseCase(query)
                }

                // Применяем фильтр по статусу
                sourceFlow.map { list ->
                    when (filter) {
                        "Открытые" -> list.filter { it.status == DefectStatus.OPEN }
                        "Закрытые" -> list.filter { it.status == DefectStatus.CLOSED }
                        else -> list
                    }
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Ошибка загрузки") }
            }.collect { defects ->
                _uiState.update {
                    it.copy(
                        defects = defects,
                        isLoading = false,
                        emptyMessage = if (defects.isEmpty()) {
                            if (searchQuery.value.isNotBlank()) "Ничего не найдено" else "Дефектов пока нет"
                        } else null
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
        selectedFilter.value = filter
    }
}
