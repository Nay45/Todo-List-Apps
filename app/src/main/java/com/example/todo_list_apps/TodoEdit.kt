package com.example.todo_list_apps

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.todo_list_apps.databinding.ActivityTodoEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TodoEdit : AppCompatActivity() {

    val db by lazy { TodoDB(this)}
    private var Id: Int = 0

    private lateinit var binding : ActivityTodoEditBinding

    private var spinnerCategory: Spinner? = null

    lateinit var pickDateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListener()

        pickDateBtn = findViewById(R.id.idBtnPickDate)

        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    pickDateBtn.text =
                        (dayOfMonth.toString() + "\t-\t" + (monthOfYear + 1) + "\t-\t" + year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            Constant.TYPE_CREATE -> {
                binding.btnUpdate.visibility = View.GONE
                binding.editTask.visibility = View.GONE
                binding.linear2.visibility = View.GONE

                setDataSpinnerCategory()
            }
            Constant.TYPE_READ -> {
                binding.newTask.visibility = View.GONE
                binding.editTask.visibility = View.GONE
                binding.linear1.visibility = View.GONE
                getTodo()
            }
            Constant.TYPE_UPDATE -> {
                binding.btnSave.visibility = View.GONE
                binding.newTask.visibility = View.GONE
                binding.linear2.visibility = View.GONE

                setDataSpinnerCategory()

                getTodo()
            }
        }
    }

    private fun setDataSpinnerCategory() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.categoryList, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory?.adapter = adapter
    }

    private fun setupListener() {
        binding.btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.todoDao().addTodo(
                    MyTodo(0, binding.edtTitle.text.toString(), binding.edtDesc.text.toString(), binding.idBtnPickDate.text.toString(), binding.spinnerCategory.selectedItem.toString())
                )
                finish()
            }
        }

        binding.btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.todoDao().updateTodo(
                    MyTodo(Id, binding.edtTitle.text.toString(), binding.edtDesc.text.toString(), binding.idBtnPickDate.text.toString(), binding.spinnerCategory.selectedItem.toString())
                )
                finish()
            }
        }
    }

    fun getTodo(){
        Id = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val todo = db.todoDao().getTodo(Id)[0]
            binding.viewTitle.setText(todo.title)
            binding.viewDesc.setText(todo.desc)
            binding.viewDate.setText(todo.date)
            binding.viewCategory.setText(todo.category)

            binding.edtTitle.setText(todo.title)
            binding.edtDesc.setText(todo.desc)
            binding.idBtnPickDate.setText(todo.date)
            binding.spinnerCategory.setSelection(getIndex(binding.spinnerCategory, todo.category))

        }
    }

    private fun getIndex(spinner: Spinner, category: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(category, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}