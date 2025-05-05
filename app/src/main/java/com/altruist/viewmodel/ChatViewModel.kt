package com.altruist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.datastore.UserSession
import com.altruist.data.model.User
import com.altruist.data.model.chat.Message
import com.altruist.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userSession: UserSession,
    private val userRepository: UserRepository // Asegúrate de tener esto implementado
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _currentMessage = MutableStateFlow(TextFieldValue(""))
    val currentMessage: StateFlow<TextFieldValue> = _currentMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val currentUserId = MutableStateFlow<Long?>(null)

    private val _receiverUser = MutableStateFlow<User?>(null)
    val receiverUser: StateFlow<User?> = _receiverUser

    init {
        viewModelScope.launch {
            userSession.getUser().collect { user ->
                currentUserId.value = user?.id_user
            }
        }
    }

    fun onMessageChange(newMessage: TextFieldValue) {
        _currentMessage.value = newMessage
    }

    fun loadMessages(receiverUserId: Long, postId: Long) {
        val senderId = currentUserId.value ?: return
        val chatId = getChatId(senderId, receiverUserId, postId)

        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _errorMessage.value = "Error al cargar mensajes: ${error.message}"
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messagesList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Message::class.java)
                    }
                    _messages.value = messagesList
                }
            }
    }

    fun sendMessage(receiverUserId: Long, postId: Long) {
        val senderId = currentUserId.value ?: return
        val content = _currentMessage.value.text.trim()
        if (content.isEmpty()) return

        val chatId = getChatId(senderId, receiverUserId, postId)

        val message = Message(
            senderId = senderId,
            receiverId = receiverUserId,
            content = content,
            timestamp = System.currentTimeMillis()
        )

        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                _currentMessage.value = TextFieldValue("")
            }
            .addOnFailureListener { error ->
                _errorMessage.value = "No se pudo enviar el mensaje: ${error.message}"
            }
    }

    fun loadReceiverUser(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.getUserById(id).onSuccess { user ->
                _receiverUser.value = user
            }.onFailure {
                _errorMessage.value = "Error al cargar usuario receptor: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    private fun getChatId(user1: Long, user2: Long, postId: Long): String {
        val (first, second) = if (user1 < user2) user1 to user2 else user2 to user1
        return "$first-$second-post$postId"
    }
}
