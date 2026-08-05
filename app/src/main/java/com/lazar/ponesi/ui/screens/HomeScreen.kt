package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazar.ponesi.data.model.Travel
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.TravelCard
import com.lazar.ponesi.ui.components.TravelStatus
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun HomeScreen(
    onDocumentsClick: () -> Unit,
    onCreateTravelClick: () -> Unit
) {

    val travels = listOf(

        Travel(
            id = 1,
            name = "Letovanje Grčka",
            status = TravelStatus.INACTIVE
        ),

        Travel(
            id = 2,
            name = "Vikend Zlatibor",
            status = TravelStatus.SCHEDULED
        ),

        Travel(
            id = 3,
            name = "Poslovni put",
            status = TravelStatus.ACTIVE
        )

    )


    Scaffold(
        topBar = {
            AppTopBar(
                title = "Putovanja",
                showFilter = true,
                showSort = true
            )
        },

        bottomBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDocumentsClick()
                    }
                ) {

                    Text(
                        text = "Dokumenta"
                    )

                }


                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onCreateTravelClick()
                    }
                ) {

                    Text(
                        text = "Novi šablon"
                    )

                }

            }

        }

    ) { paddingValues ->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {


            items(travels) { travel ->


                TravelCard(
                    title = travel.name,
                    status = travel.status,

                    onClick = {
                        println("Kliknuto ${travel.name}")
                    },

                    onEditClick = {
                        println("Edit ${travel.name}")
                    }
                )


            }

        }

    }

}