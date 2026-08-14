package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.SavedDocument
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun DocumentCard(
    document: SavedDocument,
    onOpenClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val documentType = when (document.mimeType) {
        "application/pdf" ->
            stringResource(R.string.document_type_pdf)

        else ->
            stringResource(R.string.document_type_image)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenClick),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(
            Dimens.ThinBorderWidth,
            MaterialTheme.colorScheme.outline
        ),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = document.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = documentType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = onOpenClick
            ) {
                Text(
                    text = stringResource(
                        R.string.action_open_document
                    )
                )
            }

            DeleteIconButton(
                onClick = onDeleteClick,
                contentDescription = stringResource(
                    R.string.action_delete_document
                )
            )
        }
    }
}