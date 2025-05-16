package com.altruist.data.repository

import com.altruist.data.model.Post
import com.altruist.data.network.ApiService
import com.altruist.data.network.dto.post.CreatePostRequest
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import javax.inject.Inject


class PostRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun createPost(createPostRequest: CreatePostRequest): Result<Long> {
        return try {
            val response = api.createPost(createPostRequest)
            if (response.isSuccessful) {
                val postId = response.body() ?: return Result.failure(Exception("Respuesta vacía"))
                Result.success(postId)
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

    suspend fun deletePostById(id: Long): Result<Unit> {
        return try {
            val response = api.deletePostById(id)
            if (response.isSuccessful) {
                Result.success(Unit)
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



    suspend fun getPostById(id: Long): Result<Post> {
        return try {
            val response = api.getPostById(id)
            if (response.isSuccessful) {
                val post = response.body() ?: return Result.failure(Exception("Respuesta vacía"))
                Result.success(post)
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

    suspend fun getFilteredPosts(
        idCategory: Long,
        latitude: Double,
        longitude: Double,
        maxDistanceKm: Double
    ): Result<List<Post>> {
        return try {
            val posts = api.getPostsByFilters(idCategory, latitude, longitude, maxDistanceKm)
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPostsByUser(idUser: Long): Result<List<Post>> {
        return try {
            val response = api.getPostsByUser(idUser)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                Result.success(posts)
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


}