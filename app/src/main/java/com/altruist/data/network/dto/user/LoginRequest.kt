package com.altruist.data.network.dto.user

data class LoginRequest(
    val email: String,
    val password_hash: String
)