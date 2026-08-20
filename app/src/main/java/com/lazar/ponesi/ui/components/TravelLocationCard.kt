package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.TravelLocation
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun TravelLocationCard(
    location: TravelLocation?,
    onAddOrChangeClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(
            Dimens.ThinBorderWidth,
            MaterialTheme.colorScheme.outlineVariant
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.SpacingSmall
                )
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(
                        R.string.title_destination
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (location == null) {
                Text(
                    text = stringResource(
                        R.string.message_no_destination
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAddOrChangeClick
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_add_location
                        )
                    )
                }
            } else {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimens.SpacingMedium
                    )
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onAddOrChangeClick
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_change_location
                            )
                        )
                    }

                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = onRemoveClick
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_remove_location
                            )
                        )
                    }
                }
            }
        }
    }
}