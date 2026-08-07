package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.ui.theme.TravelActiveColor
import com.lazar.ponesi.ui.theme.TravelInactiveColor
import com.lazar.ponesi.ui.theme.TravelScheduledColor
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.TravelStatus
import com.lazar.ponesi.data.model.Travel

@Composable
fun TravelCard(
    travel: Travel,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = travel.name,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.action_edit)
                    )
                }
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}