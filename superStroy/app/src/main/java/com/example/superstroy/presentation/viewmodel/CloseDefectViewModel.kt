package com.example.superstroy.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.usecase.CloseDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloseDefectViewModel @Inject constructor(
    private val closeDefectUseCase: CloseDefectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val defectId: String = savedStateHandle["defectId"] ?: ""

    fun closeDefect(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val isSuccess = closeDefectUseCase(defectId)
            if (isSuccess) onSuccess()
        }
    }
}
