package com.example.homework1.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [TodoItemEntity::class],
    version = 2,
    exportSchema = true
)
abstract class TodoDatabase : RoomDatabase() {
    
    abstract fun todoItemDao(): TodoItemDao
    
    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        
        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        // Миграция с версии 1 на версию 2
        // Пример: добавление нового поля (если нужно)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Если нужно добавить новое поле, используйте:
                // database.execSQL("ALTER TABLE todo_items ADD COLUMN new_field TEXT")
                // В данном случае миграция пустая, так как структура не изменилась
                // Но она демонстрирует, как делать миграции
            }
        }
    }
}

