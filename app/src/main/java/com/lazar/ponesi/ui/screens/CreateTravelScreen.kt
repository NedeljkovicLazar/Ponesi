package com.lazar.ponesi.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.lazar.ponesi.ui.components.AppTopBar


@Composable
fun CreateTravelScreen(
    onBackClick: () -> Unit
) {

    Scaffold(

        topBar = {

            AppTopBar(
                title = "Novi šablon",
                showBackButton = true,
                onBackClick = onBackClick
            )

        }

    ) { paddingValues ->

        Text(
            text = "Kreiranje putovanja",
            modifier = Modifier.padding(paddingValues)
        )

    }

}