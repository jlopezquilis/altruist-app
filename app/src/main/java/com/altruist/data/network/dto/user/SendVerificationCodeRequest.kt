package com.altruist.data.network.dto.user

data class SendVerificationCodeRequest(
    val email: String,
    val code: String
)