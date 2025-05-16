package com.altruist.data.model.chat

data class Message(
    val senderId: Long = 0,
    val receiverId: Long = 0,
    val content: String = "",
    val timestamp: Long = 0L
)