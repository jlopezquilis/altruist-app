package com.altruist.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor() : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _uploadedImageUrls = MutableStateFlow<List<String>>(emptyList())
    val uploadedImageUrls: StateFlow<List<String>> = _uploadedImageUrls

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _createPost1Success = MutableStateFlow(false)
    val createPost1Success: StateFlow<Boolean> = _createPost1Success

    fun onTitleChange(value: String) {
        _title.value = value
        _errorMessage.value = null
    }

    fun onCategoryChange(value: String) {
        _category.value = value
        _errorMessage.value = null
    }

    fun addImage(uri: Uri) {
        _imageUris.value = _imageUris.value + uri
    }

    fun removeImage(uri: Uri) {
        _imageUris.value = _imageUris.value - uri
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun isDataValid(): Boolean {
        return _title.value.isNotBlank() && _category.value.isNotBlank()
    }

    fun onAddImageClick(uri: Uri?) {
        uri?.let {
            _imageUris.value = _imageUris.value + it
        }
    }

    fun uploadImagesToFirebase(
        context: Context,
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

    fun onContinueFromCreatePost1Click(context: Context) {
        when {
            _title.value.isBlank() || _category.value.isBlank() -> {
                _errorMessage.value = "Por favor, completa todos los campos."
            }

            else -> {
                _isLoading.value = true
                uploadImagesToFirebase(
                    context = context,
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

    fun resetCreatePost1Success() {
        _createPost1Success.value = false
    }
}
