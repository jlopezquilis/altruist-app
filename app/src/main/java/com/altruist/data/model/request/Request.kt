package com.altruist.data.model.request
import com.altruist.data.model.Post
import com.altruist.data.model.User
import java.time.LocalDateTime

data class Request(
    val id: RequestId,
    val user: User,
    val post: Post,
    val title: String,
    val body: String?,
    val image: String?,
    val status: String?,
    val dateCreated: LocalDateTime
)
