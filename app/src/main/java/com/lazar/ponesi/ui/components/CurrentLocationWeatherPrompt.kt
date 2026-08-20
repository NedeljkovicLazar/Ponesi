package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R
import com.lazar.ponesi.ui.theme.Dimens

@Composable
fun CurrentLocationWeatherPrompt(
    onRequestLocationClick: () -> Unit
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
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(
                    R.string.title_current_location_weather
                ),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(
                    R.string.message_location_permission_weather
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRequestLocationClick
            ) {
                Text(
                    text = stringResource(
                        R.string.action_show_local_weather
                    )
                )
            }
        }
    }
}