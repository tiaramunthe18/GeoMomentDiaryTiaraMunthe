package com.example.geomomentdiary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geo_moments")
data class GeoMoment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val notes: String = "",
    val date: String = "",
    val photoPath: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)