package com.example.superstroy.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.superstroy.presentation.ui.componets.button.PrimaryButton
import com.example.superstroy.presentation.ui.componets.button.SecondaryButton
import com.example.superstroy.presentation.ui.componets.inputs.AppTextField
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.viewmodel.EditDefectViewModel

@Composable
fun EditDefectScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: EditDefectViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { AppTopBar(title = "Редактирование", onBackClick = onBackClick) }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = Dimens.SpaceL)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(Dimens.SpaceM))

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
                    enabled = false // Координаты нельзя менять вручную
                )

                // Вывод ошибки, если есть
                if (viewModel.errorMessage != null) {
                    Spacer(modifier = Modifier.height(Dimens.SpaceS))
                    Text(
                        text = viewModel.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceXL))

                // Кнопки Сохранить и Отмена
                PrimaryButton(
                    text = "Сохранить",
                    onClick = { viewModel.saveChanges(onSaveSuccess) }
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceS))

                SecondaryButton(
                    text = "Отмена",
                    onClick = onBackClick,
                    modifier = Modifier.padding(bottom = Dimens.SpaceL)
                )
            }
        }
    }
}
