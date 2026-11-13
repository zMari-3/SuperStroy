package com.example.superstroy.presentation.ui.componets.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.superstroy.presentation.ui.theme.Dimens

@Composable
fun PhotoPicker(
    imageCount: Int,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
    ) {
        // Заглушка для уже загруженных фото
        repeat(imageCount) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(Dimens.CornerS))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
        // Кнопка добавления
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(Dimens.CornerS))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(Dimens.CornerS))
                .clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить фото",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}