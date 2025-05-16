package com.altruist.utils.dto

import com.altruist.data.model.Post
import com.altruist.data.model.request.Request

data class UserPostUI(
    val post: Post,
    val requests: List<Request>
)
