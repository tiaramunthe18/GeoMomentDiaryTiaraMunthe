package com.example.geomomentdiary.ui.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.geomomentdiary.ui.GeoMomentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: GeoMomentViewModel,
    onLocationClick: (Int) -> Unit
) {

    val moments by viewModel.moments.collectAsState(initial = emptyList())

    val defaultLocation = LatLng(3.5952, 98.6722)

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(moments) {

        if (moments.isNotEmpty()) {

            val first = moments.first()

            if (first.latitude != 0.0 && first.longitude != 0.0) {

                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(first.latitude, first.longitude),
                        14f
                    )
                )

            }

        } else {

            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    defaultLocation,
                    10f
                )
            )

        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            cameraPositionState = cameraPositionState
        ) {

            moments.forEach { moment ->

                if (moment.latitude != 0.0 && moment.longitude != 0.0) {

                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                moment.latitude,
                                moment.longitude
                            )
                        ),
                        title = moment.title,
                        snippet = moment.address
                    )

                }

            }

        }

        HorizontalDivider()

        Text(
            text = "Lokasi Tersimpan",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {

            items(moments, key = { it.id }) { moment ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onLocationClick(moment.id)
                    }
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = if (moment.address.isBlank())
                                    "Alamat tidak tersedia"
                                else
                                    moment.address,
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

        }

    }

}