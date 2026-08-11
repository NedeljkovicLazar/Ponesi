package com.lazar.ponesi.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DeleteIconButton(
    onClick: () -> Unit,
    contentDescription: String
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.error
        )
    }
}