package com.example.homework1.data.remote

import com.example.homework1.TodoItem
import com.example.homework1.data.remote.api.TodoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class RemoteDataSource(
    private val api: TodoApi = NetworkModule.todoApi
) {
    
    suspend fun loadItems(): List<TodoItem> = withContext(Dispatchers.IO) {
        try {
            Timber.d("RemoteDataSource: загрузка дел с бэкенда")
            val items = api.getList()
            Timber.d("RemoteDataSource: получено ${items.size} дел с бэкенда")
            items
        } catch (e: HttpException) {
            Timber.e(e, "RemoteDataSource: HTTP ошибка при загрузке списка: ${e.code()}")
            throw NetworkException("Ошибка загрузки списка: ${e.code()}", e)
        } catch (e: IOException) {
            Timber.e(e, "RemoteDataSource: сетевая ошибка при загрузке списка")
            throw NetworkException("Сетевая ошибка при загрузке списка", e)
        } catch (e: Exception) {
            Timber.e(e, "RemoteDataSource: неожиданная ошибка при загрузке списка")
            throw NetworkException("Неожиданная ошибка при загрузке списка", e)
        }
    }

    suspend fun getItem(uid: String): TodoItem? = withContext(Dispatchers.IO) {
        try {
            Timber.d("RemoteDataSource: загрузка дела $uid с бэкенда")
            val item = api.getItem(uid)
            Timber.d("RemoteDataSource: дело $uid загружено с бэкенда")
            item
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Timber.d("RemoteDataSource: дело $uid не найдено на бэкенде")
                return@withContext null
            }
            Timber.e(e, "RemoteDataSource: HTTP ошибка при загрузке дела $uid: ${e.code()}")
            throw NetworkException("Ошибка загрузки дела: ${e.code()}", e)
        } catch (e: IOException) {
            Timber.e(e, "RemoteDataSource: сетевая ошибка при загрузке дела $uid")
            throw NetworkException("Сетевая ошибка при загрузке дела", e)
        } catch (e: Exception) {
            Timber.e(e, "RemoteDataSource: неожиданная ошибка при загрузке дела $uid")
            throw NetworkException("Неожиданная ошибка при загрузке дела", e)
        }
    }

    suspend fun saveItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            try {
                Timber.d("RemoteDataSource: отправка дела ${item.uid} на бэкенд")
                // Проверяем, существует ли элемент на сервере
                val existingItem = try {
                    api.getItem(item.uid)
                } catch (e: HttpException) {
                    if (e.code() == 404) null else throw e
                } catch (e: Exception) {
                    null // Если ошибка при проверке, пробуем создать
                }
                
                if (existingItem != null) {
                    api.updateItem(item.uid, item)
                    Timber.d("RemoteDataSource: дело ${item.uid} обновлено на бэкенде")
                } else {
                    api.addItem(item)
                    Timber.d("RemoteDataSource: дело ${item.uid} создано на бэкенде")
                }
            } catch (e: HttpException) {
                Timber.e(e, "RemoteDataSource: HTTP ошибка при сохранении дела ${item.uid}: ${e.code()}")
                throw NetworkException("Ошибка сохранения дела: ${e.code()}", e)
            } catch (e: IOException) {
                Timber.e(e, "RemoteDataSource: сетевая ошибка при сохранении дела ${item.uid}")
                throw NetworkException("Сетевая ошибка при сохранении дела", e)
            } catch (e: Exception) {
                Timber.e(e, "RemoteDataSource: неожиданная ошибка при сохранении дела ${item.uid}")
                throw NetworkException("Неожиданная ошибка при сохранении дела", e)
            }
        }
    }

    suspend fun deleteItem(uid: String) {
        withContext(Dispatchers.IO) {
            try {
                Timber.d("RemoteDataSource: удаление дела $uid с бэкенда")
                api.deleteItem(uid)
                Timber.d("RemoteDataSource: дело $uid удалено с бэкенда")
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Timber.d("RemoteDataSource: дело $uid уже удалено с бэкенда")
                    return@withContext
                }
                Timber.e(e, "RemoteDataSource: HTTP ошибка при удалении дела $uid: ${e.code()}")
                throw NetworkException("Ошибка удаления дела: ${e.code()}", e)
            } catch (e: IOException) {
                Timber.e(e, "RemoteDataSource: сетевая ошибка при удалении дела $uid")
                throw NetworkException("Сетевая ошибка при удалении дела", e)
            } catch (e: Exception) {
                Timber.e(e, "RemoteDataSource: неожиданная ошибка при удалении дела $uid")
                throw NetworkException("Неожиданная ошибка при удалении дела", e)
            }
        }
    }
}

class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)

