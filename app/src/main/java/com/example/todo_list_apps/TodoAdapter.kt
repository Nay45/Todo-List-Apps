package com.example.todo_list_apps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_main.view.*

class TodoAdapter (private var items: ArrayList<MyTodo>, private val listener: OnAdapterListener) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.adapter_main,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = items[position]
        holder.view.txtTitle.text = todo.title
        holder.view.txtCategory.text = todo.category
        holder.view.txtDesc.text = todo.desc
        holder.view.txtDate.text = todo.date

        holder.view.setOnClickListener {
            listener.onRead(todo)
        }
        holder.view.iconUpdate.setOnClickListener {
            listener.onUpdate(todo)
        }
        holder.view.iconDel.setOnClickListener {
            listener.onDelete(todo)
        }

    }

    inner class TodoViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(newList: List<MyTodo>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onRead(todo: MyTodo)
        fun onUpdate(todo: MyTodo)
        fun onDelete(todo: MyTodo)
    }
}