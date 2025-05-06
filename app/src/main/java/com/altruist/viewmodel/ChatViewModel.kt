package com.altruist.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.datastore.UserSession
import com.altruist.data.model.User
import com.altruist.data.model.chat.Message
import com.altruist.data.repository.PostRepository
import com.altruist.data.repository.UserRepository
import com.altruist.utils.generateChatId
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
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

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

    private val _shouldNavigateBack = MutableStateFlow(false)
    val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack

    init {
        viewModelScope.launch {
            userSession.getUser().collect { user ->
                currentUserId.value = user?.id_user
            }
        }
    }

    fun onIsLoadingChange(b: Boolean) {
        _isLoading.value = b
    }

    fun onMessageChange(newMessage: TextFieldValue) {
        _currentMessage.value = newMessage
    }

    fun loadMessages(receiverUserId: Long, postId: Long) {
        val senderId = currentUserId.value ?: return
        val chatId = generateChatId(receiverUserId, senderId, postId)

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

        val chatId = generateChatId(receiverUserId, senderId, postId)

        val message = Message(
            senderId = senderId,
            receiverId = receiverUserId,
            content = content,
            timestamp = System.currentTimeMillis()
        )

        // Aegurar que el documento de chat exista
        val chatRef = firestore.collection("chats").document(chatId)
        chatRef.get().addOnSuccessListener { docSnapshot ->
            if (!docSnapshot.exists()) {
                // Crea el documento del chat con un campo auxiliar
                chatRef.set(
                    mapOf(
                        "createdAt" to System.currentTimeMillis(),
                        "participants" to listOf(senderId, receiverUserId),
                        "postId" to postId
                    )
                )
            }

            // Añadir el mensaje
            chatRef.collection("messages")
                .add(message)
                .addOnSuccessListener {
                    _currentMessage.value = TextFieldValue("")
                }
                .addOnFailureListener { error ->
                    _errorMessage.value = "No se pudo enviar el mensaje: ${error.message}"
                }
        }.addOnFailureListener { error ->
            _errorMessage.value = "Error al preparar el chat: ${error.message}"
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

    fun closeDonation(receiverUserId: Long, senderUserId: Long, idPost: Long) {
        val chatId = generateChatId(receiverUserId, senderUserId, idPost)

        viewModelScope.launch {
            _isLoading.value = true

            postRepository.deletePostById(idPost).onFailure { error ->
                _errorMessage.value = "Error al eliminar el post: ${error.message}"
                _isLoading.value = false
                return@launch
            }

            val chatRef = firestore.collection("chats").document(chatId)
            chatRef.collection("messages")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val batch = firestore.batch()
                    for (doc in querySnapshot.documents) {
                        batch.delete(doc.reference)
                    }
                    batch.commit().addOnSuccessListener {
                        chatRef.delete().addOnSuccessListener {
                            _shouldNavigateBack.value = true
                            _isLoading.value = false
                        }.addOnFailureListener {
                            _errorMessage.value = "Error al eliminar el chat"
                            _isLoading.value = false
                        }
                    }.addOnFailureListener {
                        _errorMessage.value = "Error al eliminar los mensajes"
                        _isLoading.value = false
                    }
                }
                .addOnFailureListener {
                    _errorMessage.value = "Error al obtener los mensajes"
                    _isLoading.value = false
                }
        }
    }

    fun clearNavigationFlag() {
        _shouldNavigateBack.value = false
    }

}
