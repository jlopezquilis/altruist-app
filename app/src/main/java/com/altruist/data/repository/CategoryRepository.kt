package com.altruist.data.repository

import com.altruist.data.model.Category
import com.altruist.data.network.ApiService
import org.json.JSONObject
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val response = api.getAllCategories()
            if (response.isSuccessful) {
                val responseBody = response.body()
                    ?: return Result.failure(Exception("Respuesta vacía"))

                val categories = responseBody.map {
                    Category(
                        id_category = it.id_category,
                        name = it.name,
                        description = it.description,
                        icon_url = it.icon_url
                    )
                }

                Result.success(categories)
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
