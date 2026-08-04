package com.lazar.ponesi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun BottomActionButtons(
    onDocumentsClick: () -> Unit,
    onCreateTemplateClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            modifier = Modifier.weight(1f),
            onClick = onDocumentsClick
        ) {
            Text(
                text = "Dokumenta"
            )
        }


        Button(
            modifier = Modifier.weight(1f),
            onClick = onCreateTemplateClick
        ) {
            Text(
                text = "Novi šablon"
            )
        }

    }
}