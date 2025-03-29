package com.altruist.data.network.dto.auth

data class LoginRequest(
    val email: String,
    val password_hash: String
)