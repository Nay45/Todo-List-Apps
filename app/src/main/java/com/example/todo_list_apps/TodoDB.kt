package com.example.todo_list_apps

import android.content.Context
import androidx.room.*

@Database(
    entities = [MyTodo::class],
    version = 1
)

abstract class TodoDB: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile private var instance: TodoDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            TodoDB::class.java,
            "todo.db"
        ).build()
    }
}