package com.altruist.data.repository

import com.altruist.data.datastore.UserSession
import com.altruist.data.model.User
import com.altruist.data.network.ApiService
import com.altruist.data.network.dto.user.LoginRequest
import com.altruist.data.network.dto.user.LoginResponse
import com.altruist.data.network.dto.user.SendVerificationCodeRequest
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val userSession: UserSession
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginData = response.body()!!
                // Convertimos LoginResponse en User
                val user = User(
                    id_user = loginData.id_user,
                    name = loginData.name,
                    surname = loginData.surname,
                    username = loginData.username,
                    gender = loginData.gender,
                    email = loginData.email,
                    password_hash = loginData.password_hash,
                    situation = loginData.situation,
                    profile_picture_url = loginData.profile_picture_url,
                    anonymous = loginData.anonymous
                )
                userSession.saveUser(user)
                Result.success(loginData)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody != null) {
                    try {
                        JSONObject(errorBody).optString("message", "Error desconocido")
                    } catch (e: Exception) {
                        "Error al interpretar el mensaje de error"
                    }
                } else {
                    "Error desconocido"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun sendVerificationCode(email: String, code: String): Result<Unit> {
        return try {
            val response = api.sendVerificationCode(SendVerificationCodeRequest(email, code))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al enviar código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    fun getLoggedInUser(): Flow<User?> = userSession.getUser()
}

