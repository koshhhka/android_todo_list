package com.example.homework1.data.remote

import com.example.homework1.JsonConfig
import com.example.homework1.data.remote.api.TodoApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

object NetworkModule {
    
    private const val BASE_URL = "https://beta.mrdekk.ru/"
    
    @Volatile
    private var bearerToken: String? = null
    
    fun setBearerToken(token: String) {
        bearerToken = token
    }
    
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        
        bearerToken?.let { token ->
            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }
        
        chain.proceed(requestBuilder.build())
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            JsonConfig.appJson.asConverterFactory("application/json".toMediaType())
        )
        .build()
    
    val todoApi: TodoApi = retrofit.create()
}

