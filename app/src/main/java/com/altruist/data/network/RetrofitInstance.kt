package com.altruist.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//TODO: Es un Singleton. Con Kotlin pueden crearse la clase y su instancia única a la vez con la keyword "object" --> Poner como patron en memoria
//Aqui creamos la instancia (cuando se requiera, por eso el lazy) de Retrofit. Es en el builder donde Retrofit creará las implementaciones de las funciones que hemos puesto en ApiService
object RetrofitInstance {
    //LOCALHOST para emulador (Android Studio)
    private const val BASE_URL = "http://10.0.2.2:8080/"
    //GOOGLE CLOUD API
    //private const val BASE_URL = "https://altruist-api-206922921928.europe-west1.run.app"


    //lazy means its only initialized the first time its used
    //Aqui, cuando se llame a crear la vairable, primero se creará el objeto Retrofit, que creará a su ver un objeto ApiService con la implementación de las funciones indicadas
    //en la ApiService interface. Este objeto es el que se le devuelve a la variable val api.
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //JSON Objects will be converted into Kotlin objects using Gson
            .build()
            .create(ApiService::class.java)
    }
}
