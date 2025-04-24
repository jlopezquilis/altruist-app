package com.altruist.data.network

import com.altruist.data.model.Post
import com.altruist.data.model.User
import com.altruist.data.network.dto.AllCategoriesResponse
import com.altruist.data.network.dto.post.CreatePostRequest
import com.altruist.data.network.dto.user.AvailabilityResponse
import com.altruist.data.network.dto.user.CreateUserRequest
import com.altruist.data.network.dto.user.SendVerificationCodeRequest
import com.altruist.data.network.dto.user.LoginRequest
import com.altruist.data.network.dto.user.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

//Se crea la interfaz que después Rertofit tomará para crear la implementación que gesionará los POST, GET, etc. y mappeará los objetos JSON a Kotlin.
//Usando Retrofit nos ahorramos mucho código
interface ApiService {

    //USERS
    @POST("api/users/login")
    //suspend for coroutine behaviour
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/users/send-verification-code")
    suspend fun sendVerificationCode(
        @Body request: SendVerificationCodeRequest
    ): Response<Unit>

    @GET("api/users/check-email")
    suspend fun checkEmailExists(@Query("email") email: String): Response<AvailabilityResponse>

    @GET("api/users/check-username")
    suspend fun checkUsernameExists(@Query("username") username: String): Response<AvailabilityResponse>

    @POST("api/users/create")
    suspend fun createUser(@Body user: CreateUserRequest): Response<CreateUserRequest>

    //CATEGORIES
    @GET("api/categories")
    suspend fun getAllCategories(): Response<List<AllCategoriesResponse>>

    //POSTS
    @POST("api/posts/createPost")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<Long>

    @GET("api/posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Response<Post>

    @GET("api/posts/filter")
    suspend fun getPostsByFilters(
        @Query("idCategory") idCategory: Long,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("maxDistanceKm") maxDistanceKm: Double
    ): List<Post>

}