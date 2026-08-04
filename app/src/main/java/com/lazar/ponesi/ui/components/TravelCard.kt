package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class TravelStatus {
    INACTIVE,
    ACTIVE,
    SCHEDULED
}

@Composable
fun TravelCard(
    title: String,
    status: TravelStatus,
    daysUntil: Int? = null,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {

    val borderColor = when (status) {
        TravelStatus.INACTIVE -> Color.Gray
        TravelStatus.ACTIVE -> Color(0xFF4CAF50)
        TravelStatus.SCHEDULED -> Color(0xFF2196F3)
    }

    val statusText = when (status) {
        TravelStatus.INACTIVE -> "Neaktivno"
        TravelStatus.ACTIVE -> "Aktivno"
        TravelStatus.SCHEDULED ->
            if (daysUntil != null)
                "Za $daysUntil dana"
            else
                "Zakazano"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Izmeni"
                    )
                }
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}