package com.altruist.data.repository

import com.altruist.data.network.ApiService
import com.altruist.data.network.RetrofitInstance
import com.altruist.data.network.dto.auth.LoginRequest
import com.altruist.data.network.dto.auth.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }
}
