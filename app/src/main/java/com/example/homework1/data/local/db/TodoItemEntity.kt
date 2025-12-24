package com.example.homework1.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homework1.Importance
import java.time.LocalDateTime

@Entity(tableName = "todo_items")
data class TodoItemEntity(
    @PrimaryKey
    val uid: String,
    val text: String,
    val importance: String, // Store as String for Room
    val color: Int,
    val deadLine: String?, // Store as String for Room
    val isDone: Boolean
) {
    fun toTodoItem(): com.example.homework1.TodoItem {
        return com.example.homework1.TodoItem(
            uid = uid,
            text = text,
            importance = Importance.valueOf(importance),
            color = color,
            deadLine = deadLine?.let { LocalDateTime.parse(it) },
            isDone = isDone
        )
    }
    
    companion object {
        fun fromTodoItem(item: com.example.homework1.TodoItem): TodoItemEntity {
            return TodoItemEntity(
                uid = item.uid,
                text = item.text,
                importance = item.importance.name,
                color = item.color,
                deadLine = item.deadLine?.toString(),
                isDone = item.isDone
            )
        }
    }
}

