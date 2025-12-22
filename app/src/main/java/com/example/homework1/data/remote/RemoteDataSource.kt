package com.example.homework1.data.remote

import com.example.homework1.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

class RemoteDataSource {
    
    suspend fun loadItems(): List<TodoItem> = withContext(Dispatchers.IO) {
        Timber.d("RemoteDataSource: загрузка дел с бэкенда (заглушка)")
        // Имитация сетевой задержки
        delay(500)
        // Заглушка - возвращаем пустой список
        // В реальной реализации здесь будет HTTP запрос
        Timber.d("RemoteDataSource: получено 0 дел с бэкенда (заглушка)")
        emptyList()
    }

    suspend fun getItem(uid: String): TodoItem? = withContext(Dispatchers.IO) {
        Timber.d("RemoteDataSource: загрузка дела $uid с бэкенда (заглушка)")
        delay(300)
        // Заглушка - возвращаем null
        Timber.d("RemoteDataSource: дело $uid не найдено на бэкенде (заглушка)")
        null
    }

    suspend fun saveItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            Timber.d("RemoteDataSource: отправка дела ${item.uid} на бэкенд (заглушка)")
            delay(400)
            // Заглушка - просто логируем
            // В реальной реализации здесь будет HTTP POST/PUT запрос
            Timber.d("RemoteDataSource: дело ${item.uid} отправлено на бэкенд (заглушка)")
        }
    }

    suspend fun deleteItem(uid: String) {
        withContext(Dispatchers.IO) {
            Timber.d("RemoteDataSource: удаление дела $uid с бэкенда (заглушка)")
            delay(300)
            // Заглушка - просто логируем
            // В реальной реализации здесь будет HTTP DELETE запрос
            Timber.d("RemoteDataSource: дело $uid удалено с бэкенда (заглушка)")
        }
    }
}

