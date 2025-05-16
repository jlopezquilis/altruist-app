package com.altruist.data.network.dto.post

data class CreatePostRequest(
    val title: String,
    val description: String? = null,
    val status: String? = null,
    val quality: String? = null,
    val latitude: Double,
    val longitude: Double,
    val id_category: Long,
    val id_user: Long,
    val imageUrls: List<String> = emptyList()
)
