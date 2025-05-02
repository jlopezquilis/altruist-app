package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.datastore.UserSession
import com.altruist.data.model.Category
import com.altruist.data.model.Post
import com.altruist.data.model.User
import com.altruist.data.model.request.Request
import com.altruist.data.repository.PostRepository
import com.altruist.data.repository.RequestRepository
import com.altruist.utils.dto.UserPostUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class UserPostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val requestRepository: RequestRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _userPosts = MutableStateFlow<List<UserPostUI>>(emptyList())
    val userPosts: StateFlow<List<UserPostUI>> = _userPosts

    val filteredUserPosts: StateFlow<List<UserPostUI>> = combine(_userPosts, _searchQuery) { posts, query ->
        if (query.isBlank()) posts
        else posts.filter { it.post.title.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _userPostApplicantsSelected = MutableStateFlow<UserPostUI>(UserPostUI(Post(
        id_post = 0,
        title = "",
        description = "",
        status = "",
        quality = "",
        latitude = 0.0,
        longitude = 0.0,
        date_created = Date().toString(),
        category = Category(0, "", "", ""),
        user = User(0, "", "", "", "", "", "", "", "", false),
        imageUrls = emptyList(),
        distanceFromFilter = 0.0,
    ), emptyList()))
    val userPostApplicantsSelected: StateFlow<UserPostUI> = _userPostApplicantsSelected

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        loadUserPosts()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onUserPostApplicantsSelectedChange(userPostUI: UserPostUI) {
        _userPostApplicantsSelected.value = userPostUI
    }

    fun loadUserPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = userSession.getUser().firstOrNull()
                if (user == null) {
                    _errorMessage.value = "No se pudo obtener el usuario actual"
                    return@launch
                }

                val result = postRepository.getPostsByUser(user.id_user)
                if (result.isFailure) {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al cargar publicaciones"
                    return@launch
                }

                val posts = result.getOrNull().orEmpty()

                val uiPosts = posts.map { post ->
                    val requests = requestRepository.getRequestsByPost(post.id_post).getOrElse { emptyList() }
                    UserPostUI(post = post, requests = requests)
                }

                _userPosts.value = uiPosts

            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun deletePostFromList(postId: Long) {
        _userPosts.value = _userPosts.value.filterNot { it.post.id_post == postId }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            val result = postRepository.deletePostById(postId)
            if (result.isSuccess) {
                _userPosts.value = _userPosts.value.filter { it.post.id_post != postId }
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error al eliminar el post"
            }
        }
    }


    fun getRequestsForPost(postId: Long): List<Request> {
        return _userPosts.value.find { it.post.id_post == postId }?.requests ?: emptyList()
    }
}