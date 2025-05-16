package com.altruist.data.network.dto.request

data class CreateSimplifiedRequestRequest (
    val id_user: Long,
    val id_post: Long,
    val status: String? = null,
)