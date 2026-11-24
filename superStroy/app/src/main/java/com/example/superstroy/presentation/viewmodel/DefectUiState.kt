package com.example.superstroy.presentation.viewmodel

import com.example.superstroy.domain.model.Defect

data class DefectUiState(
    val defects: List<Defect> = emptyList(),
    val isLoading: Boolean = true,
    val emptyMessage: String? = null,
    val offlineMessage: String? = null,
    val permissionMessage: String? = null,
    val errorMessage: String? = null
)
