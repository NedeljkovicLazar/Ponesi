package com.lazar.ponesi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.TravelHistory
import com.lazar.ponesi.ui.theme.Dimens
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HistoryCard(
    history: TravelHistory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(
            Dimens.ThinBorderWidth,
            MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                Dimens.SpacingMedium
            )
        ) {
            HistoryThumbnail(
                photoUri = history.photoUri
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = history.title,
                    style = MaterialTheme.typography.titleMedium
                )

                history.locationName?.let { locationName ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = locationName,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                }

                Text(
                    text = formatHistoryPeriod(history),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = pluralStringResource(
                        id = R.plurals.history_duration_days,
                        count = history.durationDays.toInt(),
                        history.durationDays
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = stringResource(
                    R.string.action_open_history
                )
            )
        }
    }
}

@Composable
private fun HistoryThumbnail(
    photoUri: String?
) {
    val modifier = Modifier
        .size(92.dp)
        .clip(
            RoundedCornerShape(Dimens.ButtonCornerRadius)
        )

    if (photoUri == null) {
        Box(
            modifier = modifier.background(
                MaterialTheme.colorScheme.surfaceVariant
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        AsyncImage(
            model = photoUri.toUri(),
            contentDescription = null,
            modifier = modifier.height(92.dp),
            contentScale = ContentScale.Crop
        )
    }
}

fun formatHistoryPeriod(history: TravelHistory): String {
    val locale = Locale.forLanguageTag("sr-Latn-RS")
    val start = history.startDate
    val end = history.endDate

    val startMonth = start.month.getDisplayName(
        TextStyle.FULL_STANDALONE,
        locale
    ).lowercase(locale)

    val endMonth = end.month.getDisplayName(
        TextStyle.FULL_STANDALONE,
        locale
    ).lowercase(locale)

    return when {
        start.year == end.year &&
                start.month == end.month -> {
            "$startMonth ${start.year}"
        }

        start.year == end.year -> {
            "$startMonth–$endMonth ${start.year}"
        }

        else -> {
            "$startMonth ${start.year} – " +
                    "$endMonth ${end.year}"
        }
    }
}