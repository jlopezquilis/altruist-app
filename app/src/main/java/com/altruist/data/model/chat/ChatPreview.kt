package com.altruist.data.model.chat

import com.altruist.data.model.Post
import com.altruist.data.model.User

data class ChatPreview(
    val chatId: String,
    val lastMessage: Message,
    val relatedPost: Post,
    val otherUser: User
)
