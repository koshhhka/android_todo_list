package com.example.homework1.data.local

import com.example.homework1.TodoItem
import com.example.homework1.data.local.db.TodoDatabase
import com.example.homework1.data.local.db.TodoItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocalDataSource(private val database: TodoDatabase) {
    
    private val dao = database.todoItemDao()
    
    val items: Flow<List<TodoItem>> = dao.getAllItems().map { entities ->
        entities.map { it.toTodoItem() }
    }

    suspend fun loadItems() {
        // Данные загружаются автоматически через Flow в items
        // Этот метод оставлен для совместимости с Repository
        Timber.d("LocalDataSource: данные загружаются через Flow")
    }

    suspend fun getItem(uid: String): TodoItem? {
        return withContext(Dispatchers.IO) {
            runCatching {
                dao.getItem(uid)?.toTodoItem()
            }.getOrElse { e ->
                Timber.e(e, "LocalDataSource: ошибка при получении дела $uid")
                null
            }
        }
    }

    suspend fun saveItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            runCatching {
                dao.insertItem(TodoItemEntity.fromTodoItem(item))
                Timber.d("LocalDataSource: сохранено дело ${item.uid} в базу данных")
            }.onFailure { e ->
                Timber.e(e, "LocalDataSource: ошибка при сохранении в базу данных")
            }
        }
    }

    suspend fun deleteItem(uid: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                dao.deleteItemByUid(uid)
                Timber.d("LocalDataSource: удалено дело $uid из базы данных")
            }.onFailure { e ->
                Timber.e(e, "LocalDataSource: ошибка при удалении из базы данных")
            }
        }
    }

    suspend fun saveItems(items: List<TodoItem>) {
        withContext(Dispatchers.IO) {
            runCatching {
                val entities = items.map { TodoItemEntity.fromTodoItem(it) }
                dao.insertItems(entities)
                Timber.d("LocalDataSource: сохранено ${items.size} дел в базу данных")
            }.onFailure { e ->
                Timber.e(e, "LocalDataSource: ошибка при сохранении списка в базу данных")
            }
        }
    }
}

