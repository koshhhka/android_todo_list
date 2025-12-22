package com.example.homework1.data.remote.api

import com.example.homework1.TodoItem
import retrofit2.http.*

interface TodoApi {
    
    @GET("list")
    suspend fun getList(): List<TodoItem>
    
    @GET("list/{id}")
    suspend fun getItem(@Path("id") id: String): TodoItem
    
    @POST("list")
    suspend fun addItem(@Body item: TodoItem): TodoItem
    
    @PUT("list/{id}")
    suspend fun updateItem(@Path("id") id: String, @Body item: TodoItem): TodoItem
    
    @DELETE("list/{id}")
    suspend fun deleteItem(@Path("id") id: String)
}

