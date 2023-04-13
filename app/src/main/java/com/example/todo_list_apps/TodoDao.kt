package com.example.todo_list_apps

import androidx.room.*

@Dao
interface TodoDao {

    @Insert
    fun addTodo(Todo: MyTodo)

    @Update
    fun updateTodo(Todo: MyTodo)

    @Delete
    fun deleteTodo(Todo: MyTodo)

    @Query("SELECT * FROM MyTodo")
    fun getTodo(): List<MyTodo>

    @Query("SELECT * FROM MyTodo WHERE id=:Todo_id")
    fun getTodo(Todo_id: Int): List<MyTodo>
}