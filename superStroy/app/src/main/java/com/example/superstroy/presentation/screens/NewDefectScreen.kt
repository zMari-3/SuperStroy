package com.example.superstroy.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.superstroy.presentation.ui.componets.button.PrimaryButton
import com.example.superstroy.presentation.ui.componets.inputs.AppTextField
import com.example.superstroy.presentation.ui.componets.inputs.PhotoThumbnailRow
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.viewmodel.NewDefectViewModel

@Composable
fun NewDefectScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: NewDefectViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { AppTopBar(title = "Новый дефект", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.SpaceL)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Фото", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(Dimens.SpaceXS))
            PhotoThumbnailRow(photos = listOf("1", "2"), onAddClick = {})

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            // Привязываем значения и обработчики к ViewModel
            AppTextField(
                value = viewModel.title,
                onValueChange = viewModel::onTitleChange,
                label = "Название"
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            AppTextField(
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                label = "Описание"
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            AppTextField(
                value = viewModel.responsible,
                onValueChange = viewModel::onResponsibleChange,
                label = "Ответственный"
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            AppTextField(
                value = viewModel.term,
                onValueChange = viewModel::onTermChange,
                label = "Срок устранения"
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            AppTextField(
                value = viewModel.geolocation,
                onValueChange = {},
                label = "Геопозиция",
                enabled = false // Только для чтения
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceXL))

            PrimaryButton(
                text = "Сохранить",
                onClick = {
                    viewModel.saveDefect(onSaveClick)
                },
                enabled = !viewModel.isSaving,
                modifier = Modifier.padding(bottom = Dimens.SpaceL)
            )
        }
    }
}
