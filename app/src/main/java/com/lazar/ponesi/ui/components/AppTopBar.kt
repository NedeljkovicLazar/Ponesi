package com.lazar.ponesi.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lazar.ponesi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,

    showBackButton: Boolean = false,
    showFilter: Boolean = false,
    showSort: Boolean = false,
    showEdit: Boolean = false,

    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
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
                        contentDescription = stringResource(
                            R.string.action_back
                        )
                    )

                }

            }

        },

        actions = {

            if (showEdit) {

                IconButton(
                    onClick = onEditClick
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(
                            R.string.action_edit
                        )
                    )

                }

            }

            if (showFilter) {

                IconButton(
                    onClick = onFilterClick
                ) {

                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(
                            R.string.action_filter
                        )
                    )

                }

            }

            if (showSort) {

                IconButton(
                    onClick = onSortClick
                ) {

                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = stringResource(
                            R.string.action_sort
                        )
                    )

                }

            }

        }

    )
}