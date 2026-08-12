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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.ui.theme.TravelActiveColor
import com.lazar.ponesi.ui.theme.TravelInactiveColor
import com.lazar.ponesi.ui.theme.TravelScheduledColor
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun TravelCard(
    travel: Travel,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val borderColor = when (travel.status) {
        TravelStatus.INACTIVE -> TravelInactiveColor
        TravelStatus.ACTIVE -> TravelActiveColor
        TravelStatus.SCHEDULED -> TravelScheduledColor
    }

    val statusText = when (travel.status) {
        TravelStatus.INACTIVE ->
            stringResource(R.string.status_inactive)

        TravelStatus.ACTIVE ->
            stringResource(R.string.status_active)

        TravelStatus.SCHEDULED ->
            stringResource(R.string.status_scheduled)
    }

    val relativeDateText =
        if (
            travel.status == TravelStatus.SCHEDULED &&
            travel.date != null
        ) {
            val daysDifference = ChronoUnit.DAYS.between(
                LocalDate.now(),
                travel.date
            ).toInt()

            when {
                daysDifference > 0 -> {
                    pluralStringResource(
                        id = R.plurals.travel_days_until,
                        count = daysDifference,
                        daysDifference
                    )
                }

                daysDifference == 0 -> {
                    stringResource(
                        R.string.status_travel_today
                    )
                }

                else -> {
                    val elapsedDays = -daysDifference

                    pluralStringResource(
                        id = R.plurals.travel_days_since,
                        count = elapsedDays,
                        elapsedDays
                    )
                }
            }
        } else {
            null
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(
            Dimens.StatusBorderWidth,
            borderColor
        ),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = travel.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                EditIconButton(
                    onClick = onEditClick,
                    contentDescription = stringResource(
                        R.string.action_edit
                    )
                )

                DeleteIconButton(
                    onClick = onDeleteClick,
                    contentDescription = stringResource(
                        R.string.action_delete_template
                    )
                )
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium
            )

            relativeDateText?.let { dateText ->
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = borderColor
                )
            }
        }
    }
}