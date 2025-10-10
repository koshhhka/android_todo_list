package com.example.homework1

import android.content.Context
import android.graphics.Color
import java.time.LocalDateTime
import timber.log.Timber

class FileStorage(private val context: Context) {

    private val _items = mutableListOf<TodoItem>()
    val items: List<TodoItem> get() = _items.toList()

    init {
        Timber.d("FileStorage создан")
    }

    fun add(item: TodoItem) {
        _items.add(item)
        Timber.d("Добавлена задача: ${item.text}, UID: ${item.uid}")
    }

    fun delete(uid: String): Boolean {
        val removed = _items.removeIf { it.uid == uid }
        if (removed) {
            Timber.d("Удалена задача с UID: $uid")
        } else {
            Timber.w("Не удалось удалить задачу с UID: $uid (не найдена)")
        }
        return removed
    }

    fun saveToFile(fileName: String = "todo_items.txt") {
        try {
            val lines = _items.map { item ->
                "${item.uid}|${item.text}|${item.importance}|${item.color}|${item.deadLine}|${item.isDone}"
            }

            val fileContent = lines.joinToString("\n")
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { it.write(fileContent.toByteArray()) }

            Timber.d("Файл сохранен: $fileName, количество задач: ${_items.size}")
        } catch (e: Exception) {
            Timber.e(e, "Ошибка при сохранении файла")
        }
    }

    fun loadFromFile(fileName: String = "todo_items.txt") {
        try {
            val inputStream = context.openFileInput(fileName)
            val fileContent = inputStream.bufferedReader().use { it.readText() }

            val loadedItems = fileContent.lines().mapNotNull { line ->
                if (line.isBlank()) return@mapNotNull null
                val parts = line.split("|")
                if (parts.size != 6) return@mapNotNull null

                TodoItem(
                    uid = parts[0],
                    text = parts[1],
                    importance = Importance.valueOf(parts[2]),
                    color = parts[3].toInt(),
                    deadLine = if (parts[4] == "null") null else LocalDateTime.parse(parts[4]),
                    isDone = parts[5].toBoolean()
                )
            }

            _items.clear()
            _items.addAll(loadedItems)

            Timber.d("Файл загружен: $fileName, количество задач: ${_items.size}")
        } catch (e: Exception) {
            Timber.e(e, "Ошибка при загрузке файла")
        }
    }
}
