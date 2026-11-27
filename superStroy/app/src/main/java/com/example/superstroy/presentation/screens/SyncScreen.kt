package com.example.superstroy.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.presentation.ui.componets.button.SecondaryButton
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.viewmodel.SyncViewModel

@Composable
fun SyncScreen(
    onBackClick: () -> Unit,
    viewModel: SyncViewModel = hiltViewModel()
) {
    val defects by viewModel.defects.collectAsState()
    val syncState by viewModel.syncState.collectAsState()

    val unsyncedCount = defects.count { !it.isSynced }

    Scaffold(topBar = { AppTopBar("Синхронизация", onBackClick) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.SpaceL)
        ) {
            Icon(
                Icons.Default.CloudUpload,
                modifier = Modifier.align(Alignment.CenterHorizontally).size(80.dp),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Text(
                "Синхронизация данных",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = if (syncState is SyncViewModel.SyncState.Syncing)
                    "Отправка данных..."
                else
                    "Ожидают отправки: $unsyncedCount записей",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            // Прогресс-бар
            if (syncState is SyncViewModel.SyncState.Syncing) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                LinearProgressIndicator(
                    progress = { if (defects.isEmpty()) 1f
                    else (defects.count { it.isSynced }.toFloat() / defects.size) },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(defects, key = { it.id }) { defect ->
                    SyncCard(defect)
                }
            }
            SecondaryButton(
                text = "Повторить синхронизацию",
                onClick = { viewModel.startSync() }
            )
        }
    }
}

@Composable
fun SyncCard(defect: Defect) {
    val statusColor = if (defect.isSynced) Color(0xFF15803D) else Color(0xFFD97706)
    val statusText = if (defect.isSynced) "Отправлено" else "В очереди"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceS)
            .background(MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp))
            .padding(Dimens.SpaceM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = defect.title.ifBlank { "Без названия" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = statusText,
            color = statusColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
