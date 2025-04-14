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
    val id_category: Long,
    val id_user: Long,
    val images: List<String> = emptyList()
)
