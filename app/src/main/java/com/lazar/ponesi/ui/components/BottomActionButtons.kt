package com.lazar.ponesi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazar.ponesi.ui.theme.Dimens
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
@Composable
fun BottomActionButtons(
    onDocumentsClick: () -> Unit,
    onCreateTemplateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SpacingLarge),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = onDocumentsClick
        ) {
            Text(text = stringResource(R.string.button_documents))
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onCreateTemplateClick
        ) {
            Text(text = stringResource(R.string.button_new_template))
        }
    }
}