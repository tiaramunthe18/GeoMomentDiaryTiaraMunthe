package com.example.geomomentdiary.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.geomomentdiary.R

@Composable
fun HomeScreen(
    onBuatCatatan: () -> Unit,
    onCatatanSaya: () -> Unit,
    onPetaMomen: () -> Unit
) {

    Scaffold { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFF3F5EF),
                            Color.White
                        )
                    )
                )
                .padding(padding)
        ) {

            // Banner
            Image(
                painter = painterResource(R.drawable.banner),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            // Card Putih
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp),
                shape = RoundedCornerShape(
                    topStart = 36.dp,
                    topEnd = 36.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(22.dp)
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "📍 GeoMoment Diary",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF214A31)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Halo! Siap simpan momen hari ini?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF214A31)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Foto, cerita, dan lokasimu akan tersimpan dengan aman untuk dikenang kapan saja.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HomeMenuCard(
                        icon = Icons.Default.Edit,
                        title = "Buat Catatan",
                        subtitle = "Tambah momen baru",
                        background = Color(0xFFF1F5EB),
                        onClick = onBuatCatatan
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    HomeMenuCard(
                        icon = Icons.Default.List,
                        title = "Catatan Saya",
                        subtitle = "Lihat semua catatan",
                        background = Color(0xFFF7F2E8),
                        onClick = onCatatanSaya
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    HomeMenuCard(
                        icon = Icons.Default.LocationOn,
                        title = "Peta Momen",
                        subtitle = "Jelajahi semua lokasi",
                        background = Color(0xFFEAF4FA),
                        onClick = onPetaMomen
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun HomeMenuCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    background: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2E5D37),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF203A26)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

            }

            Surface(
                shape = CircleShape,
                color = Color(0xFF4F8A4E)
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )

            }

        }

    }

}