package com.example.geomomentdiary.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.geomomentdiary.data.GeoMoment
import com.example.geomomentdiary.location.LocationHelper
import com.example.geomomentdiary.utils.PhotoHelper
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: GeoMomentViewModel,
    noteId: Int?,
    onDone: () -> Unit
) {
    val moments by viewModel.moments.collectAsState(initial = emptyList())
    val existing = moments.find { it.id == noteId }

    var title by remember(noteId) { mutableStateOf("") }
    var notesText by remember(noteId) { mutableStateOf("") }

    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val coroutineScope = rememberCoroutineScope()

    var latitude by remember(noteId) { mutableStateOf(0.0) }
    var longitude by remember(noteId) { mutableStateOf(0.0) }
    var address by remember(noteId) { mutableStateOf("") }
    var isLoadingLocation by remember { mutableStateOf(false) }

    // --- State untuk foto ---
    var photoPath by remember(noteId) { mutableStateOf("") }
    var pendingPhotoFile by remember { mutableStateOf<File?>(null) }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(existing) {
        existing?.let {
            title = it.title
            notesText = it.notes
            photoPath = it.photoPath
            latitude = it.latitude
            longitude = it.longitude
            address = it.address
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingPhotoFile != null) {
            photoPath = pendingPhotoFile!!.absolutePath
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = PhotoHelper.createImageFile(context)
            val uri = PhotoHelper.getUriForFile(context, file)
            pendingPhotoFile = file
            pendingPhotoUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun onTakePhotoClick() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            val file = PhotoHelper.createImageFile(context)
            val uri = PhotoHelper.getUriForFile(context, file)
            pendingPhotoFile = file
            pendingPhotoUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // --- Lokasi (sudah ada dari sebelumnya) ---
    fun fetchLocation() {
        isLoadingLocation = true
        coroutineScope.launch {
            val result = locationHelper.getCurrentLocation()
            if (result != null) {
                latitude = result.first
                longitude = result.second
                address = locationHelper.getAddressFromLatLng(latitude, longitude)
            } else {
                address = "Lokasi tidak ditemukan"
            }
            isLoadingLocation = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fetchLocation()
        } else {
            address = "Izin lokasi ditolak"
        }
    }

    LaunchedEffect(Unit) {
        if (existing == null) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                fetchLocation()
            } else {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(if (existing == null) "Buat Catatan" else "Edit Catatan") })

        Column(Modifier.padding(16.dp)) {
            Text("Judul")
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Masukkan judul catatan") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text("Isi Catatan")
            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                placeholder = { Text("Tulis catatanmu di sini...") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text("Foto")
            Spacer(Modifier.height(8.dp))

            if (photoPath.isNotBlank()) {
                val bitmap = remember(photoPath) {
                    BitmapFactory.decodeFile(photoPath)
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Preview foto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

            OutlinedButton(
                onClick = { onTakePhotoClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (photoPath.isBlank()) "Ambil Foto" else "Ambil Ulang Foto")
            }

            Spacer(Modifier.height(16.dp))

            Text("Lokasi")
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isLoadingLocation) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Mengambil lokasi...")
                } else {
                    Text(address.ifBlank { "Belum ada lokasi" })
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {

                    val currentDate = SimpleDateFormat(
                        "dd MMMM yyyy • HH:mm",
                        Locale("id", "ID")
                    ).format(Date())

                    val moment = GeoMoment(
                        id = existing?.id ?: 0,
                        title = title,
                        notes = notesText,
                        date = existing?.date ?: currentDate,
                        photoPath = photoPath,
                        address = address,
                        latitude = latitude,
                        longitude = longitude
                    )

                    if (existing == null) {
                        viewModel.addMoment(moment)
                    } else {
                        viewModel.updateMoment(moment)
                    }

                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}