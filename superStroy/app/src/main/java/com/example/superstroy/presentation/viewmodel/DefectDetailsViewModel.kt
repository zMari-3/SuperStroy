package com.example.superstroy.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.usecase.GetDefectByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefectDetailsViewModel @Inject constructor(
    private val getDefectByIdUseCase: GetDefectByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val defectId: String = savedStateHandle["defectId"] ?: ""
    var defect by mutableStateOf<Defect?>(null)
        private set

    init {
        viewModelScope.launch {
            defect = getDefectByIdUseCase(defectId)
        }
    }
}
