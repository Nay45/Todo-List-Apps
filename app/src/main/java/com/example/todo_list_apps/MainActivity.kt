package com.example.todo_list_apps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_list_apps.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var todoAdapter: TodoAdapter

    val db by lazy { TodoDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        loadtodo()
    }

    fun loadtodo(){
        CoroutineScope(Dispatchers.IO).launch {
            val todos = db.todoDao().getTodo()
            Log.d("MainActivity", "dbResponse: $todos")
            withContext(Dispatchers.Main){
                todoAdapter.setData(todos)
            }
        }
    }

    private fun setupListener() {
        binding.buttonCreate.setOnClickListener {
            //startActivity(Intent(this, EditActivity::class.java))
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(todoId: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, TodoEdit::class.java)
                .putExtra("intent_id", todoId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(arrayListOf(), object : TodoAdapter.OnAdapterListener{
            override fun onRead(todo: MyTodo) {
                intentEdit(todo.id, Constant.TYPE_READ)
            }

            override fun onUpdate(todo: MyTodo) {
                intentEdit(todo.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(todo: MyTodo) {
                deleteDialog(todo)
            }

        })
        binding.listNote.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = todoAdapter
        }
    }

    private fun deleteDialog(todo: MyTodo){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Sure to remove \"${todo.title}\"?" )
            setNegativeButton("Cancle") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Remove") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.todoDao().deleteTodo(todo)
                    loadtodo()
                }
            }
        }
        alertDialog.show()
    }
}