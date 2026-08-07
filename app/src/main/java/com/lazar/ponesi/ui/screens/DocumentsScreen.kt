package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazar.ponesi.ui.components.AppTopBar
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R

@Composable
fun DocumentsScreen(
    onBackClick: () -> Unit
) {

    Scaffold(

        topBar = {

            AppTopBar(
                title = stringResource(R.string.title_documents),
                showBackButton = true,
                onBackClick = onBackClick
            )

        }

    ) { paddingValues ->

        Text(
            text = stringResource(R.string.documents_placeholder),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

    }

}