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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.superstroy.presentation.ui.componets.button.SecondaryButton
import com.example.superstroy.presentation.ui.componets.navigation.AppTopBar
import com.example.superstroy.presentation.ui.theme.Dimens
import com.example.superstroy.presentation.ui.theme.StatusOpenText
import com.example.superstroy.presentation.ui.theme.SuccessLight

@Composable
fun SyncScreen(onBackClick: () -> Unit, onSyncClick: () -> Unit) {
    Scaffold(topBar = { AppTopBar("Синхронизация", onBackClick) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.SpaceL)
        ) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = "Синхронизация",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Text(
                "Синхронизация данных",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                "Отправка 3 записей на сервер...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            SyncCard(title = "Дефект #124", status = "Отправлено", statusColor = SuccessLight)
            SyncCard(title = "Дефект #123", status = "Отправлено", statusColor = SuccessLight)
            SyncCard(title = "Дефект #122", status = "В очереди", statusColor = StatusOpenText)

            Spacer(modifier = Modifier.weight(1f))
            SecondaryButton(text = "Повторить синхронизацию", onClick = onSyncClick)
        }
    }
}

@Composable
fun SyncCard(title: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceS)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(Dimens.SpaceM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Text(status, color = statusColor, style = MaterialTheme.typography.labelLarge)
    }
}
