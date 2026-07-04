package com.example.geomomentdiary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geomomentdiary.data.GeoMoment
import com.example.geomomentdiary.data.GeoMomentRepository
import kotlinx.coroutines.launch

class GeoMomentViewModel(private val repo: GeoMomentRepository) : ViewModel() {

    val moments = repo.allMoments

    fun addMoment(moment: GeoMoment) = viewModelScope.launch { repo.insert(moment) }
    fun updateMoment(moment: GeoMoment) = viewModelScope.launch { repo.update(moment) }
    fun deleteMoment(moment: GeoMoment) = viewModelScope.launch { repo.delete(moment) }
}