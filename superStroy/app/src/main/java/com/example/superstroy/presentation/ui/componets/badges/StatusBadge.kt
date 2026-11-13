package com.example.superstroy.presentation.ui.componets.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.presentation.ui.theme.*

@Composable
fun StatusBadge(status: DefectStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, text) = when (status) {
        DefectStatus.OPEN -> Triple(StatusOpenBg, StatusOpenText, "Открыт")
        DefectStatus.CLOSED -> Triple(StatusClosedBg, StatusClosedText, "Закрыт")
    }

    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}