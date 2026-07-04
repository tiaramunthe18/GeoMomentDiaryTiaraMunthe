package com.example.geomomentdiary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.geomomentdiary.data.GeoMoment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: GeoMomentViewModel,
    onNoteClick: (GeoMoment) -> Unit
) {
    var keyword by remember { mutableStateOf("") }
    val moments by viewModel.moments.collectAsState(initial = emptyList())

    val filteredMoments = if (keyword.isBlank()) {
        moments
    } else {
        moments.filter {
            it.title.contains(keyword, ignoreCase = true) ||
                    it.notes.contains(keyword, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopAppBar(
            title = {
                Text("Catatan Saya")
            }
        )

        OutlinedTextField(
            value = keyword,
            onValueChange = {
                keyword = it
            },
            placeholder = {
                Text("Cari catatan...")
            },
            leadingIcon = {
                Icon(Icons.Default.Search, null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(
                items = filteredMoments,
                key = { it.id }
            ) { moment ->

                NoteItem(
                    moment = moment,
                    onClick = {
                        onNoteClick(moment)
                    }
                )

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    moment: GeoMoment,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (moment.photoPath.isNotBlank()) {

                AsyncImage(
                    model = moment.photoPath,
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = moment.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = moment.date,
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )

        }

    }

}