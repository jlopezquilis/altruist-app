package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.model.Post
import com.altruist.data.network.dto.request.CreateSimplifiedRequestRequest
import com.altruist.data.repository.PostRepository
import com.altruist.data.repository.RequestRepository
import com.altruist.utils.enums.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post

    private val _requestStatus = MutableStateFlow<RequestStatus?>(null)
    val requestStatus: StateFlow<RequestStatus?> = _requestStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _requestSuccessMessage = MutableStateFlow<String?>(null)
    val requestSuccessMessage: StateFlow<String?> = _requestSuccessMessage


    fun onPostChange(post: Post) {
        _post.value = post
    }

    fun loadPostById(postId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            postRepository.getPostById(postId)
                .onSuccess { fetchedPost ->
                    _post.value = fetchedPost
                    _errorMessage.value = null
                }
                .onFailure {
                    _errorMessage.value = "Error al cargar el post: ${it.message}"
                }
            _isLoading.value = false
        }
    }

    fun sendRequest(postId: Long, userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = CreateSimplifiedRequestRequest(userId, postId, RequestStatus.REQUESTED.toString())
                val result = requestRepository.createRequest(request)
                result.onSuccess {
                    _errorMessage.value = null
                    //Prefijo SUCCESS para alterar estilo Scaffold (mensaje flotante)
                    _requestSuccessMessage.value = "SUCCESS|Solicitud creada con éxito"
                    getRequestStatus(postId, userId)
                }.onFailure {
                    _errorMessage.value = "Error al enviar la solicitud:\n${it.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción al enviar la solicitud: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRequestStatus(id_post: Long, id_user: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                requestRepository.getRequestByUserAndPost(id_user, id_post)
                    .onSuccess { request ->
                        _requestStatus.value = request.status?.let { RequestStatus.valueOf(it) }
                    }
                    .onFailure { exception ->
                        val isNotFound = exception.message?.contains("HTTP 404") == true
                        if (!isNotFound) {
                            _errorMessage.value = "No se pudo obtener el estado de la solicitud\n${exception.message}"
                        }
                        _requestStatus.value = null
                    }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción al obtener estado: ${e.message}"
                _requestStatus.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun clearError() {
        _errorMessage.value = null
    }

    fun clearRequestSuccess() {
        _requestSuccessMessage.value = null
    }

}
