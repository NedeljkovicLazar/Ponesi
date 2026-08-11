package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun CategoryCard(
    category: Category,
    onAddItemClick: () -> Unit,
    onEditCategoryClick: () -> Unit,
    onRemoveCategoryClick: () -> Unit,
    onEditItemClick: (Int) -> Unit,
    onRemoveItemClick: (Int) -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                EditIconButton(
                    onClick = onEditCategoryClick,
                    contentDescription = stringResource(
                        R.string.title_edit_category
                    )
                )

                RemoveIconButton(
                    onClick = onRemoveCategoryClick,
                    contentDescription = stringResource(
                        R.string.action_remove_category
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(
                    Dimens.SpacingSmall
                )
            ) {
                category.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${item.name}",
                            modifier = Modifier.weight(1f)
                        )

                        EditIconButton(
                            onClick = {
                                onEditItemClick(item.id)
                            },
                            contentDescription = stringResource(
                                R.string.title_edit_item
                            )
                        )

                        RemoveIconButton(
                            onClick = {
                                onRemoveItemClick(item.id)
                            },
                            contentDescription = stringResource(
                                R.string.action_remove_item
                            )
                        )
                    }
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