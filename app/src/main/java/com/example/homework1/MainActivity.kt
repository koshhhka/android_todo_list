package com.example.homework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.time.LocalDateTime
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ТЕСТ FILESTORAGE - теперь this работает!
        testFileStorage()

        setContent {
            Text("Смотри логи")
        }
    }

    private fun testFileStorage() {
        println("=== ТЕСТ FILESTORAGE ===")

        val storage = FileStorage(this)
        val testTask1 = TodoItem(
            uid = "uid1",
            text = "Задача 1",
            importance = Importance.IMPORTANT,
            color = android.graphics.Color.RED,
            deadLine = LocalDateTime.now().plusDays(1)
        )

        val testTask2 = TodoItem(
            uid = "uid2",
            text = "Задача 2",
            importance = Importance.REGULAR,
            color = android.graphics.Color.RED,
            deadLine = LocalDateTime.now().plusDays(2)
        )

        storage.add(testTask1)
        storage.add(testTask2)

        storage.items.forEach {
            println("Задача: ${it.text}, UID: ${it.uid}")
        }

        storage.saveToFile()
        println("Файл сохранен")

        storage.loadFromFile()
        println("Загружено задач: ${storage.items.size}")

        val deleteResult = storage.delete("uid1")
        println("Удаление uid1: $deleteResult")
        println("Осталось задач: ${storage.items.size}")
    }
}