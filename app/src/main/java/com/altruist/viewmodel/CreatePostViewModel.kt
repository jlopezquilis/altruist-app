package com.altruist.viewmodel

import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.model.Category
import com.altruist.data.repository.CategoryRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import android.location.Geocoder
import android.location.Address
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.model.LatLng

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    // Lista de categorías (Tipo Category)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _uploadedImageUrls = MutableStateFlow<List<String>>(emptyList())
    val uploadedImageUrls: StateFlow<List<String>> = _uploadedImageUrls

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude: StateFlow<Double?> = _latitude

    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude: StateFlow<Double?> = _longitude

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _createPost1Success = MutableStateFlow(false)
    val createPost1Success: StateFlow<Boolean> = _createPost1Success

    private val _createPost2Success = MutableStateFlow(false)
    val createPost2Success: StateFlow<Boolean> = _createPost2Success

    private val _createPost3Success = MutableStateFlow(false)
    val createPost3Success: StateFlow<Boolean> = _createPost3Success

    private val _createPost4Success = MutableStateFlow(false)
    val createPost4Success: StateFlow<Boolean> = _createPost4Success

    init {
        viewModelScope.launch {
            loadCategories()
        }
    }

    fun onTitleChange(value: String) {
        _title.value = value
        _errorMessage.value = null
    }

    fun onCategoryChange(value: String) {
        _category.value = value
        _errorMessage.value = null
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
        _errorMessage.value = null
    }

    fun onStateChange(value: String) {
        _status.value = value
        _errorMessage.value = null
    }

    fun onLatitudeChange(value: Double) {
        _latitude.value = value
        _errorMessage.value = null
    }

    fun onLongitudeChange(value: Double) {
        _longitude.value = value
        _errorMessage.value = null
    }


    fun clearError() {
        _errorMessage.value = null
    }

    fun addImage(uri: Uri) {
        _imageUris.value = _imageUris.value + uri
    }

    fun removeImage(uri: Uri) {
        _imageUris.value = _imageUris.value - uri
    }

    fun isDataScreen1Valid(): Boolean {
        return !_isLoading.value && _title.value.isNotBlank() && _category.value.isNotBlank()
    }

    fun isDataScreen2Valid(): Boolean {
        return !_isLoading.value && _description.value.isNotBlank() && _status.value.isNotBlank()
    }

    fun onAddImageClick(uri: Uri?) {
        uri?.let {
            _imageUris.value = _imageUris.value + it
        }
    }

    fun uploadImagesToFirebase(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val uris = _imageUris.value
        if (uris.isEmpty()) {
            _uploadedImageUrls.value = emptyList()
            onSuccess()
            return
        }

        _isLoading.value = true
        val storageRef = FirebaseStorage.getInstance().reference
        val urls = mutableListOf<String>()

        var uploadedCount = 0
        uris.forEach { uri ->
            val imageRef = storageRef.child("post_images/${UUID.randomUUID()}.jpg")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            urls.add(downloadUri.toString())
                            uploadedCount++
                            if (uploadedCount == uris.size) {
                                _uploadedImageUrls.value = urls
                                _isLoading.value = false
                                onSuccess()
                            }
                        }
                        .addOnFailureListener {
                            _isLoading.value = false
                            onError("Error al obtener la URL de la imagen")
                        }
                }
                .addOnFailureListener {
                    _isLoading.value = false
                    onError("Error al subir imagen: ${it.message}")
                }
        }
    }

    suspend fun loadCategories() {
        _isLoading.value = true
        val result = categoryRepository.getAllCategories()
        _isLoading.value = false
        result.onSuccess { categories ->
            _categories.value = categories
        }.onFailure {
            _errorMessage.value = "Error al cargar las categorías: ${it.message}"}
    }


    // Función para buscar la ubicación por dirección
    fun searchLocation(query: String, geocoder: Geocoder, onResult: (LatLng?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val addresses: List<Address>? = geocoder.getFromLocationName(query, 1)

                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        val location = addresses?.get(0)
                        val latLng = location?.let { LatLng(it.latitude, location.longitude) }
                        // Actualizamos latitud y longitud
                        if (location != null) {
                            _latitude.value = location.latitude
                        }
                        if (location != null) {
                            _longitude.value = location.longitude
                        }
                        withContext(Dispatchers.Main) {
                            onResult(latLng)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onResult(null)  // Si no encontramos la ubicación, devolvemos null
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(null)  // En caso de error, devolvemos null
                }
            }
        }
    }

    fun onContinueFromCreatePost1Click(context: Context) {
        when {
            _title.value.isBlank() || _category.value.isBlank() -> {
                _errorMessage.value = "Por favor, completa todos los campos."
            }

            else -> {
                _isLoading.value = true
                uploadImagesToFirebase(
                    onSuccess = {
                        _createPost1Success.value = true
                        _isLoading.value = false
                    },
                    onError = { error ->
                        _errorMessage.value = error
                        _isLoading.value = false
                    }
                )
            }
        }
    }

    fun onContinueFromCreatePost2Click() {
        when {
            _description.value.isBlank() || _status.value.isBlank() -> {
                _errorMessage.value = "Por favor, completa todos los campos."
            }

            else -> {
                _createPost2Success.value = true
            }
        }
    }

    fun onContinueFromCreatePost3Click() {
        when {
            //Lógica
            else -> {
                _createPost3Success.value = true
            }
        }
    }

    fun onContinueFromCreatePost4Click() {
        when {
            //Lógica
            else -> {
                _createPost4Success.value = true
            }
        }
    }

    fun resetCreatePost1Success() {
        _createPost1Success.value = false
    }
    fun resetCreatePost2Success() {
        _createPost2Success.value = false
    }
    fun resetCreatePost3Success() {
        _createPost3Success.value = false
    }
}

