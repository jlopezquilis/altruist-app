package com.altruist.data.model

data class Post(
    val id_post: Long,
    val title: String,
    val description: String?,
    val status: String?,
    val quality: String?,
    val latitude: Double,
    val longitude: Double,
    val date_created: String, // Viene como String en JSON
    val categoryName: String?, // O id_category si prefieres
    val username: String?,
    val images: List<String> = emptyList()
)
