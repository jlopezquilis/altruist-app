package com.altruist.data.model

data class LoginResponse(
    val message: String,
    val userId: Long,
    val name: String
)