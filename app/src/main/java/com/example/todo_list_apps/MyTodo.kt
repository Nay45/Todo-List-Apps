package com.example.todo_list_apps

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyTodo (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val desc: String,
    val date: String,
    val category: String
)