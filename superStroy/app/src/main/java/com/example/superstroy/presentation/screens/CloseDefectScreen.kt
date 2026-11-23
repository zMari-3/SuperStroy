package com.example.superstroy.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.usecase.CloseDefectUseCase
import com.example.superstroy.presentation.ui.componets.button.PrimaryButton
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.ui.theme.SuccessLight
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

@Composable
fun CloseDefectScreen(
    onBackClick: () -> Unit,
    onCloseSuccess: () -> Unit,
    viewModel: CloseDefectViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { AppTopBar(title = "Закрытие дефекта", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.SpaceL),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заглушка под фото
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(imageVector = Icons.Default.CheckCircle,
                contentDescription = "Дефект устранен",
                tint = SuccessLight,
                modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(Dimens.SpaceM))
            Text("Дефект устранен?", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Добавьте фото после исправления",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Закрыть дефект",
                onClick = { viewModel.closeDefect(onCloseSuccess) }
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceL))
        }
    }
}