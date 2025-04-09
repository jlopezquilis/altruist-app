package com.altruist.dependecy_injection

import android.content.Context
import com.altruist.data.datastore.UserSession
import com.altruist.data.network.ApiService
import com.altruist.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Tiempo para conectar
            .readTimeout(30, TimeUnit.SECONDS)    // Tiempo para leer la respuesta
            .writeTimeout(30, TimeUnit.SECONDS)   // Tiempo para escribir el cuerpo
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://altruist-api-206922921928.europe-west1.run.app")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        userSession: UserSession
    ): UserRepository {
        return UserRepository(apiService, userSession)
    }
}
