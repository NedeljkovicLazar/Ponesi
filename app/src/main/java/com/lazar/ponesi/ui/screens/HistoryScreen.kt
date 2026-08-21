package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lazar.ponesi.R
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.HistoryCard
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    onHistoryClick: (Int) -> Unit,
    historyViewModel: HistoryViewModel
) {
    val history by historyViewModel
        .history
        .collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(
                    R.string.title_history
                ),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(Dimens.SpacingLarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.message_history_empty
                    )
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    Dimens.SpacingLarge
                ),
                verticalArrangement = Arrangement.spacedBy(
                    Dimens.SpacingMedium
                )
            ) {
                items(
                    items = history,
                    key = { historyEntry ->
                        historyEntry.id
                    }
                ) { historyEntry ->
                    HistoryCard(
                        history = historyEntry,
                        onClick = {
                            onHistoryClick(historyEntry.id)
                        }
                    )
                }
            }
        }
    }
}