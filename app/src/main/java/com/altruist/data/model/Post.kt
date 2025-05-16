package com.altruist.data.model

data class Post(
    val id_post: Long,
    val title: String,
    val description: String?,
    val status: String?,
    val quality: String?,
    val latitude: Double,
    val longitude: Double,
    val date_created: String,
    val category: Category,
    val user: User,
    val imageUrls: List<String>,
    val distanceFromFilter: Double
)
