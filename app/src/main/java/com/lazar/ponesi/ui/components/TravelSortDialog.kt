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
import com.lazar.ponesi.data.model.TravelSort
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun TravelSortDialog(
    selectedSort: TravelSort,
    onSortSelected: (TravelSort) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        TravelSort.NEWEST to
                stringResource(R.string.sort_newest),

        TravelSort.NAME to
                stringResource(R.string.sort_name),

        TravelSort.DATE to
                stringResource(R.string.sort_date),

        TravelSort.STATUS to
                stringResource(R.string.sort_status)
    )

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = stringResource(
                    R.string.title_sort_travels
                )
            )
        },

        text = {
            Column {
                options.forEach { (sort, label) ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSortSelected(sort)
                                onDismiss()
                            }
                            .padding(
                                vertical = Dimens.SpacingSmall
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedSort == sort,
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