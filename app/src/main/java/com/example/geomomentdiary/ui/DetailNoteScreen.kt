package com.example.geomomentdiary.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.geomomentdiary.data.GeoMoment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNoteScreen(
    moment: GeoMoment,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail Catatan")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = moment.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = moment.date,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (moment.photoPath.isNotBlank()) {

                val bitmap = BitmapFactory.decodeFile(moment.photoPath)

                bitmap?.let {

                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Isi Catatan",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(moment.notes)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Lokasi",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(moment.address)

            Spacer(modifier = Modifier.height(24.dp))

            FilledTonalButton(
                onClick = {

                    val uri = Uri.parse(
                        "geo:${moment.latitude},${moment.longitude}?q=${moment.latitude},${moment.longitude}"
                    )

                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage("com.google.android.apps.maps")
                    }

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://maps.google.com/?q=${moment.latitude},${moment.longitude}")
                        )
                        context.startActivity(webIntent)
                    }

                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Lihat Peta")

            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Edit")

            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Hapus")

            }

        }

    }

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Hapus Catatan")
            },
            text = {
                Text("Apakah Anda yakin ingin menghapus catatan ini?")
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Hapus")
                }

            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Batal")
                }

            }
        )

    }

}