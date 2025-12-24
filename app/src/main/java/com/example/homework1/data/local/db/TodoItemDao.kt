package com.example.homework1.data.local.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    
    @Query("SELECT * FROM todo_items")
    fun getAllItems(): Flow<List<TodoItemEntity>>
    
    @Query("SELECT * FROM todo_items WHERE uid = :uid")
    suspend fun getItem(uid: String): TodoItemEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoItemEntity)
    
    @Update
    suspend fun updateItem(item: TodoItemEntity)
    
    @Delete
    suspend fun deleteItem(item: TodoItemEntity)
    
    @Query("DELETE FROM todo_items WHERE uid = :uid")
    suspend fun deleteItemByUid(uid: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<TodoItemEntity>)
}

