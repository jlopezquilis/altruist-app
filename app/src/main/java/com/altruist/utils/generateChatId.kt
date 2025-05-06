package com.altruist.utils

fun generateChatId(userId1: Long, userId2: Long, postId: Long): String {
    val (minId, maxId) = listOf(userId1, userId2).sorted()
    return "$minId-$maxId-$postId"
}
