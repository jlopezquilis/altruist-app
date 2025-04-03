package com.altruist.data.repository

import com.altruist.data.datastore.UserSession
import com.altruist.data.model.User
import com.altruist.data.network.ApiService
import com.altruist.data.network.dto.user.CreateUserRequest
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
            if (response.isSuccessful) {
                val loginResponseBody = response.body()
                    ?: return Result.failure(Exception("Respuesta vacía"))
                // Convertimos LoginResponse en User
                val user = User(
                    id_user = loginResponseBody.id_user,
                    name = loginResponseBody.name,
                    surname = loginResponseBody.surname,
                    username = loginResponseBody.username,
                    gender = loginResponseBody.gender,
                    email = loginResponseBody.email,
                    password_hash = loginResponseBody.password_hash,
                    situation = loginResponseBody.situation,
                    profile_picture_url = loginResponseBody.profile_picture_url,
                    anonymous = loginResponseBody.anonymous
                )
                userSession.saveUser(user)
                Result.success(loginResponseBody)
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

    suspend fun createUser(user: CreateUserRequest): Result<CreateUserRequest> {
        return try {
            val response = api.createUser(user)
            if (response.isSuccessful) {
                val userResponseBody = response.body()
                    ?: return Result.failure(Exception("Respuesta vacía"))
                return Result.success(userResponseBody)
            } else {
                return Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    fun getLoggedInUser(): Flow<User?> = userSession.getUser()

    suspend fun checkEmailExists(email: String): Boolean {
        return api.checkEmailExists(email).body()?.exists ?: false
    }

    suspend fun checkUsernameExists(username: String): Boolean {
        val result = api.checkUsernameExists(username).body()?.exists
        return result ?: false
    }
}

