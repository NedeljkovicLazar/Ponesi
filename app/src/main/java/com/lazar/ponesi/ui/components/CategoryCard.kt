package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.ui.theme.Dimens
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R

@Composable
fun CategoryCard(
    category: Category,
    onAddItemClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(Dimens.CardCornerRadius),

        border = BorderStroke(
            Dimens.ThinBorderWidth,
            MaterialTheme.colorScheme.outline
        ),

        colors = CardDefaults.cardColors()
    ) {

        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                category.items.forEach { item ->
                    Text(
                        text = "• ${item.name}"
                    )
                }
            }

            Button(
                onClick = onAddItemClick
            ) {
                Text(
                    text = stringResource(R.string.action_add_item)
                )
            }
        }
    }
}