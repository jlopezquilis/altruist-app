package com.altruist.viewmodel

import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.datastore.UserSession
import com.altruist.data.model.Category
import com.altruist.data.model.Post
import com.altruist.data.repository.PostRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _selectedDistanceKm = MutableStateFlow(50f)
    val selectedDistanceKm: StateFlow<Float> = _selectedDistanceKm

    private val _locationName = MutableStateFlow("Valencia")
    val locationName: StateFlow<String> = _locationName

    //UBICACION POR DEFECTO: Valencia
    private val _latitude = MutableStateFlow(39.4699)
    val latitude: StateFlow<Double> = _latitude

    private val _longitude = MutableStateFlow(-0.3763)
    val longitude: StateFlow<Double> = _longitude

    private val _filteredPosts = MutableStateFlow<List<Post>>(emptyList())
    val filteredPosts: StateFlow<List<Post>> = _filteredPosts

    private var allPosts: List<Post> = emptyList()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    //Variables genéricas
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchPost1Success = MutableStateFlow(false)
    val searchPost1Success: StateFlow<Boolean> = _searchPost1Success

    private val _searchPost2Success = MutableStateFlow(false)
    val searchPost2Success: StateFlow<Boolean> = _searchPost2Success

    private val _currentUserId = MutableStateFlow<Long?>(null)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            userSession.getUser().collect { user ->
                _currentUserId.value = user?.id_user
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _filteredPosts.value = if (query.isBlank()) {
            allPosts
        } else {
            allPosts.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

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

    fun showError(message: String) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }


    //TODO: Get location name
    @androidx.annotation.RequiresApi(33) // Opcional si usas minSdk < 33
    fun getLocationName(geocoder: Geocoder) {
        val lat = _latitude.value
        val lon = _longitude.value

        geocoder.getFromLocation(lat, lon, 1) { addresses ->
            val city = addresses?.firstOrNull()?.locality
                ?: addresses?.firstOrNull()?.subAdminArea
                ?: addresses?.firstOrNull()?.adminArea
                ?: "Ubicación desconocida"

            _locationName.value = city
        }
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

    fun loadFilteredPosts(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            postRepository.getFilteredPosts(
                idCategory = selectedCategory.value?.id_category ?: return@launch,
                latitude = latitude.value,
                longitude = longitude.value,
                maxDistanceKm = selectedDistanceKm.value.toDouble()
            ).onSuccess { posts ->
                val currentUserId = _currentUserId.value
                val filtered = if (currentUserId != null) {
                    posts.filter { it.user.id_user != currentUserId }
                } else posts

                allPosts = filtered
                _filteredPosts.value = filtered

                onSuccess()
            }.onFailure {
                onError("Error al cargar publicaciones: ${it.message}")
            }
        }
    }


    fun onContinueFromSearchPost1Click() {
        when {
            _selectedCategory.value == null -> {
                showError("Por favor, selecciona una categoría.")
            }
            else -> {
                _searchPost1Success.value = true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onContinueFromSearchPost2Click(geocoder: Geocoder) {
        when {

            else -> {
                getLocationName(geocoder)
                _isLoading.value = true
                loadFilteredPosts(
                    onSuccess = {
                        _searchPost2Success.value = true
                    },
                    onError = { error ->
                        showError(error)
                    }
                )
                _isLoading.value = false
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
