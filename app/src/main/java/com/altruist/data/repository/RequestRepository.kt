package com.altruist.data.repository


import com.altruist.data.model.request.Request
import com.altruist.data.network.ApiService
import com.altruist.data.network.dto.request.CreateSimplifiedRequestRequest
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun createRequest(request: CreateSimplifiedRequestRequest): Result<Boolean> {
        return try {
            val response = api.createRequest(request)
            if (response.isSuccessful) {
                val result = response.body() ?: return Result.failure(Exception("Respuesta vacía"))
                Result.success(result)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = errorBody ?: "Error desconocido"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }


    suspend fun getRequestByUserAndPost(idUser: Long, idPost: Long): Result<Request> {
        return try {
            val response = api.getRequestByUserAndPost(idUser, idPost)
            if (response.isSuccessful) {
                val request = response.body() ?: return Result.failure(Exception("Respuesta vacía"))
                Result.success(request)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = errorBody ?: "Error desconocido"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun getRequestsByUser(idUser: Long): Result<List<Request>> {
        return try {
            val response = api.getRequestsByUser(idUser)
            if (response.isSuccessful) {
                val requests = response.body() ?: emptyList()
                Result.success(requests)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = errorBody ?: "Error desconocido"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun getRequestsByPost(idPost: Long): Result<List<Request>> {
        return try {
            val response = api.getRequestsByPost(idPost)
            if (response.isSuccessful) {
                val requests = response.body() ?: emptyList()
                Result.success(requests)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = errorBody ?: "Error desconocido"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun deleteRequest(idUser: Long, idPost: Long): Result<Unit> {
        return try {
            val response = api.deleteRequest(idUser, idPost)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = errorBody ?: "Error desconocido"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }
}
