package com.altruist.data.network.dto.user

data class CreateUserRequest(
    val name: String,
    val surname: String,
    val username: String,
    val gender: String,
    val email: String,
    val password_hash: String,
    val situation: String,
    val profile_picture_url: String,
    val anonymous: Boolean
)