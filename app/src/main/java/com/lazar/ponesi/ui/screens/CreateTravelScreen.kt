package com.lazar.ponesi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.lazar.ponesi.data.model.Category
import com.lazar.ponesi.data.model.PackingItem
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.CategoryCard
import com.lazar.ponesi.ui.components.PrimaryButton
import com.lazar.ponesi.ui.theme.Dimens
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R

@Composable
fun CreateTravelScreen(
    onBackClick: () -> Unit
) {

    var travelName by rememberSaveable {
        mutableStateOf("")
    }

    val categories = remember {
        listOf(
            Category(
                id = 1,
                name = "Kofer",
                items = listOf(
                    PackingItem(
                        id = 1,
                        name = "Majice"
                    ),
                    PackingItem(
                        id = 2,
                        name = "Pantalone"
                    ),
                    PackingItem(
                        id = 3,
                        name = "Donji veš"
                    )
                )
            ),

            Category(
                id = 2,
                name = "Ranac",
                items = listOf(
                    PackingItem(
                        id = 4,
                        name = "Punjač"
                    ),
                    PackingItem(
                        id = 5,
                        name = "Slušalice"
                    )
                )
            ),

            Category(
                id = 3,
                name = "Neseser",
                items = listOf(
                    PackingItem(
                        id = 6,
                        name = "Četkica za zube"
                    ),
                    PackingItem(
                        id = 7,
                        name = "Pasta za zube"
                    )
                )
            )
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_new_template),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {

            item {

                OutlinedTextField(
                    value = travelName,
                    onValueChange = { newName ->
                        travelName = newName
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.label_template_name)
                        )
                    },
                    singleLine = true
                )

            }

            item {

                Text(
                    text = stringResource(R.string.label_categories)
                )

            }

            items(categories) { category ->

                CategoryCard(
                    category = category,
                    onAddItemClick = {
                        println("Dodaj stavku u ${category.name}")
                    }
                )

            }

            item {

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        println("Dodaj kategoriju")
                    }
                ) {

                    Text(
                        text = stringResource(R.string.action_add_category)
                    )

                }

            }

            item {

                PrimaryButton(
                    text = stringResource(R.string.action_save),
                    onClick = {
                        println("Sačuvaj šablon: $travelName")
                    }
                )

            }

        }

    }

}