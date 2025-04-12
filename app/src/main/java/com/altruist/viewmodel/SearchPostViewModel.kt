package com.altruist.viewmodel

import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.altruist.data.model.Category
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchPostViewModel @Inject constructor() : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _searchPost1Success = MutableStateFlow(false)
    val searchPost1Success: StateFlow<Boolean> = _searchPost1Success

    private val _searchPost2Success = MutableStateFlow(false)
    val searchPost2Success: StateFlow<Boolean> = _searchPost2Success

    private val _latitude = MutableStateFlow(39.4699) // Valencia por defecto
    val latitude: StateFlow<Double> = _latitude

    private val _longitude = MutableStateFlow(-0.3763)
    val longitude: StateFlow<Double> = _longitude

    private val _selectedDistanceKm = MutableStateFlow(50f)
    val selectedDistanceKm: StateFlow<Float> = _selectedDistanceKm

    fun onLocationSelected(lat: Double, lon: Double) {
        _latitude.value = lat
        _longitude.value = lon
    }

    fun onDistanceSelected(distance: Float) {
        _selectedDistanceKm.value = distance
    }

    fun onCategoryChange(category: Category) {
        _selectedCategory.value = category
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun searchLocation(query: String, geocoder: Geocoder, onResult: (LatLng?) -> Unit) {
        try {
            val addresses = geocoder.getFromLocationName(query, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                onResult(LatLng(location.latitude, location.longitude))
            } else {
                onResult(null)
            }
        } catch (e: Exception) {
            onResult(null)
        }
    }

    fun onContinueFromSearchPost1Click() {
        when {
            _selectedCategory.value == null -> {
                _errorMessage.value = "Por favor, selecciona una categoría."
            }
            else -> {
                _searchPost1Success.value = true
            }
        }
    }

    fun onContinueFromSearchPost2Click() {
        when {

            else -> {
                _searchPost2Success.value = true
            }
        }
    }

    fun resetSearchPost1Success() {
        _searchPost1Success.value = false
    }

    fun resetSearchPost2Success() {
        _searchPost2Success.value = false
    }
}
