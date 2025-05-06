package com.altruist.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.datastore.UserSession
import com.altruist.data.model.User
import com.altruist.data.model.chat.ChatPreview
import com.altruist.data.model.chat.Message
import com.altruist.data.repository.PostRepository
import com.altruist.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val userSession: UserSession,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _chatPreviews = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chatPreviews: StateFlow<List<ChatPreview>> = _chatPreviews

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val currentUserId = MutableStateFlow<Long?>(null)

    init {
        viewModelScope.launch {
            userSession.getUser().collect { user ->
                currentUserId.value = user?.id_user
                user?.let { loadChats(it.id_user) }
            }
        }
    }

    fun onIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    private fun loadChats(userId: Long) {
        _isLoading.value = true

        firestore.collection("chats")
            .get()
            .addOnSuccessListener { snapshot ->
                val chatIds = snapshot.documents.mapNotNull { it.id }
                    .filter { id ->
                        val parts = id.split("-post")
                        if (parts.size != 2) return@filter false
                        val userParts = parts[0].split("-")
                        userParts.size == 2 && userParts.any { it == userId.toString() }
                    }

                chatIds.forEach { chatId ->
                    val parts = chatId.split("-post")
                    val userParts = parts[0].split("-")
                    val postId = parts[1].toLongOrNull() ?: return@forEach

                    val receiverId = userParts[0].toLongOrNull()
                    val senderId = userParts[1].toLongOrNull()

                    val otherUserId = when (userId) {
                        receiverId -> senderId
                        senderId -> receiverId
                        else -> null
                    } ?: return@forEach

                    firestore.collection("chats")
                        .document(chatId)
                        .collection("messages")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { msgSnapshot ->
                            val lastMsg = msgSnapshot.documents.firstOrNull()?.toObject(Message::class.java)
                            if (lastMsg != null) {
                                viewModelScope.launch {
                                    val postResult = postRepository.getPostById(postId)
                                    val userResult = userRepository.getUserById(otherUserId)
                                    if (postResult.isSuccess && userResult.isSuccess) {
                                        val preview = ChatPreview(
                                            chatId = chatId,
                                            lastMessage = lastMsg,
                                            relatedPost = postResult.getOrThrow(),
                                            otherUser = userResult.getOrThrow()
                                        )
                                        _chatPreviews.value = _chatPreviews.value + preview
                                    }
                                }
                            }
                        }
                }
            }
            .addOnFailureListener {
                _errorMessage.value = "No se pudieron cargar los chats: ${it.message}"
            }
            .addOnCompleteListener {
                _isLoading.value = false
            }
    }

    fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm dd/MM", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}

