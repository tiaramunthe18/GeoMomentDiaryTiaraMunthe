package com.example.geomomentdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.geomomentdiary.data.AppDatabase
import com.example.geomomentdiary.data.GeoMomentRepository
import com.example.geomomentdiary.ui.GeoMomentNavHost
import com.example.geomomentdiary.ui.GeoMomentViewModel
import com.example.geomomentdiary.ui.theme.GeoMomentDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Siapkan Database
        val database = AppDatabase.getInstance(applicationContext)

        // 2. Siapkan Repository (butuh Database)
        val repository = GeoMomentRepository(database.geoMomentDao())

        // 3. Siapkan ViewModel (butuh Repository)
        val viewModel = GeoMomentViewModel(repository)

        // 4. Tampilkan aplikasi (butuh ViewModel)
        setContent {
            GeoMomentDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GeoMomentNavHost(viewModel = viewModel)
                }
            }
        }
    }
}