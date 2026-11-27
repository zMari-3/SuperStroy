package com.example.superstroy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.data.di.NetworkError
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.usecase.GetDefectsUseCase
import com.example.superstroy.domain.usecase.SearchDefectsUseCase
import com.example.superstroy.presentation.mapper.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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

    private val refreshTrigger = MutableStateFlow(0)

    init {
        observeDefects()
    }
    fun loadDefects() {
        refreshTrigger.value++
    }

    private fun observeDefects() {
        viewModelScope.launch {
            combine(searchQuery, selectedFilter, refreshTrigger) { query, filter, _ ->
                Pair(query, filter)
            }.flatMapLatest { (query, filter) ->
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                flow {
                    try {
                        val sourceFlow = if (query.isBlank()) getDefectsUseCase() else searchDefectsUseCase(query)
                        val filteredList = sourceFlow.first().let { list ->
                            when (filter) {
                                "Открытые" -> list.filter { it.status == DefectStatus.OPEN }
                                "Закрытые" -> list.filter { it.status == DefectStatus.CLOSED }
                                else -> list
                            }
                        }
                        emit(filteredList)
                    } catch (e: Exception) {
                        val userMessage = when (e) {
                            is NetworkError -> e.toUserMessage()
                            else -> e.message ?: "Ошибка загрузки"
                        }
                        _uiState.update { it.copy(isLoading = false, errorMessage = userMessage) }
                    }
                }
            }.collect { defects ->
                _uiState.update {
                    it.copy(
                        defects = defects,
                        isLoading = false,
                        emptyMessage = if (defects.isEmpty()) {
                            if (searchQuery.value.isNotBlank()) "Ничего не найдено"
                            else "Список пуст. Создайте первую\n" +
                                    "запись о дефекте, чтобы она\n" +
                                    "появилась здесь."
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

    fun dismissOfflineMode() {
        // Убираем сообщение об ошибке, UI переключится на список из Room
        _uiState.update { it.copy(offlineMessage = null) }
    }
}
