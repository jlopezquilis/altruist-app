package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.model.Category
import com.altruist.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _locationPermissionGranted = MutableStateFlow<Boolean?>(null)
    val locationPermissionGranted: StateFlow<Boolean?> = _locationPermissionGranted

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            loadCategories()
        }
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

    private suspend fun loadCategories() {
        val result = categoryRepository.getAllCategories()
        result.onSuccess {
            _categories.value = it
        }.onFailure {
            showError("Error inesperado. Reinicia la app porfavor.")
        }
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        _locationPermissionGranted.value = granted
    }

}
