package com.altruist.data.network

import com.altruist.data.model.LoginRequest
import com.altruist.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

//Se crea la interfaz que después Rertofiz tomará para crear la implementación que gesionará los POST, GET, etc. y mappeará los objetos JSON a Kotlin.
//Usando Retrofit nos ahorramos mucho código
interface ApiService {
    @POST("api/auth/login")
    //suspend for coroutine behaviour
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}