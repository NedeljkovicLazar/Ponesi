package com.lazar.ponesi.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,

    showBackButton: Boolean = false,
    showFilter: Boolean = false,
    showSort: Boolean = false,

    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {}
) {

    CenterAlignedTopAppBar(

        title = {
            Text(
                text = title
            )
        },

        navigationIcon = {

            if (showBackButton) {

                IconButton(
                    onClick = onBackClick
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Nazad"
                    )

                }

            }

        },

        actions = {

            if (showFilter) {

                IconButton(
                    onClick = onFilterClick
                ) {

                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )

                }

            }


            if (showSort) {

                IconButton(
                    onClick = onSortClick
                ) {

                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )

                }

            }

        }

    )
}