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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.ui.theme.TravelActiveColor
import com.lazar.ponesi.ui.theme.TravelInactiveColor
import com.lazar.ponesi.ui.theme.TravelScheduledColor
import java.time.format.DateTimeFormatter

@Composable
fun TravelLifecycleCard(
    travel: Travel,
    onScheduleClick: () -> Unit,
    onCancelScheduleClick: () -> Unit,
    onStartClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    val statusColor = when (travel.status) {
        TravelStatus.INACTIVE -> TravelInactiveColor
        TravelStatus.SCHEDULED -> TravelScheduledColor
        TravelStatus.ACTIVE -> TravelActiveColor
    }

    val statusText = when (travel.status) {
        TravelStatus.INACTIVE ->
            stringResource(R.string.status_inactive)

        TravelStatus.SCHEDULED ->
            stringResource(R.string.status_scheduled)

        TravelStatus.ACTIVE ->
            stringResource(R.string.status_active)
    }

    val formattedDate = travel.date?.format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy.")
    )

    val formattedStartDate = travel.startDate?.format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy.")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(
            Dimens.StatusBorderWidth,
            statusColor
        ),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(
                Dimens.SpacingMedium
            )
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleMedium,
                color = statusColor
            )

            when (travel.status) {
                TravelStatus.INACTIVE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            Dimens.SpacingMedium
                        )
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onStartClick
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.action_start_travel
                                )
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = onScheduleClick
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.action_schedule_travel
                                )
                            )
                        }
                    }
                }

                TravelStatus.SCHEDULED -> {
                    formattedDate?.let { date ->
                        Text(
                            text = stringResource(
                                R.string.status_scheduled_for,
                                date
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onStartClick
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_start_travel
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            Dimens.SpacingMedium
                        )
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = onScheduleClick
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.action_change_date
                                )
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = onCancelScheduleClick
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.action_cancel_schedule
                                )
                            )
                        }
                    }
                }

                TravelStatus.ACTIVE -> {
                    formattedStartDate?.let { date ->
                        Text(
                            text = stringResource(
                                R.string.status_started_on,
                                date
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onFinishClick
                    ) {
                        Text(
                            text = stringResource(
                                R.string.action_finish_travel
                            )
                        )
                    }
                }
            }
        }
    }
}