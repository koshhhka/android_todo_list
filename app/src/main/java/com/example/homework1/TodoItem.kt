package com.example.homework1

import android.graphics.Color
import java.time.LocalDateTime
import java.util.UUID

enum class Importance{
    UNIMPORTANT, REGULAR, IMPORTANT
}

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance = Importance.REGULAR,
    val color: Int = Color.WHITE,
    val deadLine: LocalDateTime? = null,
    val isDone: Boolean = false,
)
