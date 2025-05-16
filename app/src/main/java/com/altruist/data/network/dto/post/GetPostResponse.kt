package com.altruist.data.network.dto.post

data class PostResponse(
    val id_post: Long,
    val title: String,
    val description: String?,
    val status: String?,
    val quality: String?,
    val latitude: Double,
    val longitude: Double,
    val date_created: String, // LocalDateTime se mapea como String en JSON
    val categoryName: String?,
    val username: String?,
    val imageUrls: List<String> = emptyList()
)
