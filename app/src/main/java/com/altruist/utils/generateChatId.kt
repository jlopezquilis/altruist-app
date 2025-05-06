package com.altruist.utils

fun generateChatId(receiverId: Long, senderId: Long, postId: Long): String {
    return "$receiverId-$senderId-post$postId"
}