package com.lazar.ponesi.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lazar.ponesi.R
import com.lazar.ponesi.data.model.SavedDocument
import com.lazar.ponesi.ui.components.AppTopBar
import com.lazar.ponesi.ui.components.DocumentCard
import com.lazar.ponesi.ui.theme.Dimens
import com.lazar.ponesi.viewmodel.DocumentsViewModel
import androidx.core.net.toUri

@Composable
fun DocumentsScreen(
    onBackClick: () -> Unit,
    documentsViewModel: DocumentsViewModel
) {
    val context = LocalContext.current

    val documents by documentsViewModel
        .documents
        .collectAsStateWithLifecycle()

    var pendingDocumentUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var pendingDocumentMimeType by remember {
        mutableStateOf("")
    }

    var documentTitle by remember {
        mutableStateOf("")
    }

    var documentPendingDeletion by remember {
        mutableStateOf<SavedDocument?>(null)
    }

    val documentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        if (uri != null) {
            try {
                context.contentResolver
                    .takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
            } catch (_: SecurityException) {
                // OpenDocument obično daje trajnu dozvolu.
                // Ako je provider ne podržava, fajl će i dalje
                // biti dostupan tokom trenutne sesije.
            }

            val displayName = getDocumentDisplayName(
                context = context,
                uri = uri
            )

            pendingDocumentUri = uri
            pendingDocumentMimeType =
                resolveDocumentMimeType(
                    context = context,
                    uri = uri,
                    displayName = displayName
                )

            documentTitle = displayName
                .substringBeforeLast(
                    delimiter = ".",
                    missingDelimiterValue = displayName
                )
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(
                    R.string.title_documents
                ),
                showBackButton = true,
                onBackClick = onBackClick
            )
        },

        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpacingLarge),
                onClick = {
                    documentPicker.launch(
                        arrayOf(
                            "application/pdf",
                            "image/jpeg",
                            "image/png"
                        )
                    )
                }
            ) {
                Text(
                    text = stringResource(
                        R.string.action_add_document
                    )
                )
            }
        }
    ) { paddingValues ->

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
            item {
                Text(
                    text = stringResource(
                        R.string.documents_description
                    )
                )
            }

            if (documents.isEmpty()) {
                item {
                    Text(
                        text = stringResource(
                            R.string.documents_empty
                        )
                    )
                }
            } else {
                items(
                    items = documents,
                    key = { document ->
                        document.id
                    }
                ) { document ->

                    DocumentCard(
                        document = document,

                        onOpenClick = {
                            openDocument(
                                context = context,
                                document = document
                            )
                        },

                        onDeleteClick = {
                            documentPendingDeletion = document
                        }
                    )
                }
            }
        }
    }

    pendingDocumentUri?.let { uri ->

        AlertDialog(
            onDismissRequest = {
                pendingDocumentUri = null
                pendingDocumentMimeType = ""
                documentTitle = ""
            },

            title = {
                Text(
                    text = stringResource(
                        R.string.title_add_document
                    )
                )
            },

            text = {
                OutlinedTextField(
                    value = documentTitle,
                    onValueChange = { newTitle ->
                        documentTitle = newTitle
                    },
                    label = {
                        Text(
                            text = stringResource(
                                R.string.label_document_title
                            )
                        )
                    },
                    singleLine = true
                )
            },

            confirmButton = {
                TextButton(
                    enabled = documentTitle.isNotBlank(),
                    onClick = {
                        documentsViewModel.addDocument(
                            title = documentTitle,
                            uri = uri.toString(),
                            mimeType = pendingDocumentMimeType
                        )

                        pendingDocumentUri = null
                        pendingDocumentMimeType = ""
                        documentTitle = ""
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_add
                        )
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        pendingDocumentUri = null
                        pendingDocumentMimeType = ""
                        documentTitle = ""
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_cancel
                        )
                    )
                }
            }
        )
    }

    documentPendingDeletion?.let { document ->

        AlertDialog(
            onDismissRequest = {
                documentPendingDeletion = null
            },

            title = {
                Text(
                    text = stringResource(
                        R.string.title_delete_document
                    )
                )
            },

            text = {
                Text(
                    text = stringResource(
                        R.string.message_delete_document,
                        document.title
                    )
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        documentsViewModel.deleteDocument(
                            document.id
                        )

                        documentPendingDeletion = null
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_delete
                        )
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        documentPendingDeletion = null
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.action_cancel
                        )
                    )
                }
            }
        )
    }
}

private fun getDocumentDisplayName(
    context: Context,
    uri: Uri
): String {
    var displayName = ""

    context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null
    )?.use { cursor ->

        val nameColumnIndex = cursor.getColumnIndex(
            OpenableColumns.DISPLAY_NAME
        )

        if (
            nameColumnIndex >= 0 &&
            cursor.moveToFirst()
        ) {
            displayName = cursor.getString(
                nameColumnIndex
            )
        }
    }

    return displayName
}

private fun resolveDocumentMimeType(
    context: Context,
    uri: Uri,
    displayName: String
): String {
    return context.contentResolver.getType(uri)
        ?: when {
            displayName.endsWith(
                suffix = ".pdf",
                ignoreCase = true
            ) -> {
                "application/pdf"
            }

            displayName.endsWith(
                suffix = ".png",
                ignoreCase = true
            ) -> {
                "image/png"
            }

            else -> {
                "image/jpeg"
            }
        }
}

private fun openDocument(
    context: Context,
    document: SavedDocument
) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(
            document.uri.toUri(),
            document.mimeType
        )

        addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            R.string.message_document_open_failed,
            Toast.LENGTH_LONG
        ).show()
    } catch (_: SecurityException) {
        Toast.makeText(
            context,
            R.string.message_document_open_failed,
            Toast.LENGTH_LONG
        ).show()
    }
}