package com.lazar.ponesi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.TravelFilter
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun TravelFilterDialog(
    selectedFilter: TravelFilter,
    onFilterSelected: (TravelFilter) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        TravelFilter.ALL to
                stringResource(R.string.filter_all),

        TravelFilter.ACTIVE to
                stringResource(R.string.filter_active),

        TravelFilter.SCHEDULED to
                stringResource(R.string.filter_scheduled),

        TravelFilter.INACTIVE to
                stringResource(R.string.filter_inactive)
    )

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = stringResource(
                    R.string.title_filter_travels
                )
            )
        },

        text = {
            Column {
                options.forEach { (filter, label) ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onFilterSelected(filter)
                                onDismiss()
                            }
                            .padding(
                                vertical = Dimens.SpacingSmall
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFilter == filter,
                            onClick = null
                        )

                        Text(
                            text = label,
                            modifier = Modifier.padding(
                                start = Dimens.SpacingSmall
                            )
                        )
                    }
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(
                        R.string.action_close
                    )
                )
            }
        }
    )
}