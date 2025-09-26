package com.example.homework1
import android.content.Context
import java.time.LocalDateTime

class FileStorage(private val context: Context) {
    private val _items = mutableListOf<TodoItem>()
    val items: List<TodoItem> get() = _items

    fun add (case: TodoItem) {
        _items.add(case)
    }

    fun delete (uid: String): Boolean {
        val foundItem = _items.find { it.uid == uid }
        if (foundItem != null) {
            _items.remove(foundItem)
            return true
        } else (
            return false
        )
    }

    fun saveToFile(fileName: String = "todo_items.txt") {
        try {
            val cases = mutableListOf<String>()

            for (item in _items) {
                val line = "${item.uid}|${item.text}|${item.importance}|${item.color}|${item.deadLine}|${item.isDone}"
                cases.add(line)
            }

            val fileContent = cases.joinToString("\n")
            val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(fileContent.toByteArray())
            outputStream.close()
        } catch (e: Exception){
            println("Ошибка при сохранении файла: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadFromFile(fileName: String = "todo_items.txt") {
        try {

            val inputStream = context.openFileInput(fileName)
            val fileContent = inputStream.bufferedReader().use { it.readText() }

            val cases = fileContent.split("\n")
            val loadedItems = mutableListOf<TodoItem>()

            for (case in cases) {
                if (case.isNotBlank()) {
                    val parts = case.split("|")
                    if (parts.size == 6) { // Проверяем, что все поля есть
                        val item = TodoItem(
                            uid = parts[0],
                            text = parts[1],
                            importance = Importance.valueOf(parts[2]),
                            color = parts[3].toInt(),
                            deadLine = if (parts[4] == "null") null else LocalDateTime.parse(parts[4]),
                            isDone = parts[5].toBoolean()
                        )
                        loadedItems.add(item)
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка при загрузке файла: ${e.message}")
        }
    }
}