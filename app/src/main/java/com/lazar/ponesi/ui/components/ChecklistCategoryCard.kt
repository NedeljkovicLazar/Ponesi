package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun ChecklistCategoryCard(
    category: Category,
    onItemCheckedChange: (
        itemId: Int,
        isChecked: Boolean
    ) -> Unit,
    onSetAllChecked: (
        isChecked: Boolean
    ) -> Unit
) {
    val allItemsChecked =
        category.items.isNotEmpty() &&
                category.items.all { item ->
                    item.isChecked
                }

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
            verticalArrangement = Arrangement.spacedBy(
                Dimens.SpacingMedium
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    enabled = category.items.isNotEmpty(),
                    onClick = {
                        onSetAllChecked(!allItemsChecked)
                    }
                ) {
                    Text(
                        text = stringResource(
                            if (allItemsChecked) {
                                R.string.action_uncheck_all
                            } else {
                                R.string.action_check_all
                            }
                        )
                    )
                }
            }

            if (category.items.isEmpty()) {
                Text(
                    text = stringResource(
                        R.string.message_category_empty
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                category.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = { isChecked ->
                                onItemCheckedChange(
                                    item.id,
                                    isChecked
                                )
                            }
                        )

                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = if (item.isChecked) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                    }
                }
            }
        }
    }
}