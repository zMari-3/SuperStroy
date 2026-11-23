package com.example.superstroy.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.usecase.GetDefectByIdUseCase
import com.example.superstroy.presentation.ui.componets.badges.StatusBadge
import com.example.superstroy.presentation.ui.componets.button.PrimaryButton
import com.example.superstroy.presentation.ui.componets.button.SecondaryButton
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
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

@Composable
fun DefectDetailsScreen(
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onCloseClick: (String) -> Unit,
    viewModel: DefectDetailsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { AppTopBar(title = viewModel.defect?.title ?: "Детали", onBackClick = onBackClick) }
    ) { padding ->
        val defect = viewModel.defect
        if (defect != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Большое фото
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Column(modifier = Modifier.padding(Dimens.SpaceL)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(defect.title, style = MaterialTheme.typography.titleLarge)
                        StatusBadge(defect.status)
                    }

                    Spacer(modifier = Modifier.height(Dimens.SpaceL))

                    Text("Описание", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(defect.description ?: "Нет описания", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(Dimens.SpaceM))

                    DetailRow("Дата создания", defect.createdDate.toString().substringBefore("T"))
                    DetailRow("Срок устранения", defect.term.toString())
                    DetailRow("Ответственный", defect.responsible)
                    DetailRow("Геопозиция", "${defect.latitude ?: 0.0}, ${defect.longitude ?: 0.0}")
                    DetailRow("Фото", if (defect.photoUrl.isNullOrBlank()) "Нет фото" else "1 изображение")

                    Spacer(modifier = Modifier.height(Dimens.SpaceXL))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
                    ) {
                        SecondaryButton(text = "Редактировать", onClick = { onEditClick(defect.id) }, modifier = Modifier.weight(1f))
                        PrimaryButton(text = "Закрыть", onClick = { onCloseClick(defect.id) }, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceS),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}