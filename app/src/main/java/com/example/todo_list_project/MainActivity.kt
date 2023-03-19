package com.example.todo_list_project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.adapter.TaskDoneAdapter
import com.example.todo_list_project.adapter.TaskNotDoneAdapter
import com.example.todo_list_project.classes.Task
import com.example.todo_list_project.handler.DatabaseHandler


class MainActivity : AppCompatActivity() {
    private lateinit var addTaskButton: ImageView
    private lateinit var doneTaskButton: Button

    companion object {
        var taskNotDoneList = mutableListOf<Task>()
        var taskDoneList = mutableListOf<Task>()
        var taskNotDoneAdapter: TaskNotDoneAdapter = TaskNotDoneAdapter(taskNotDoneList)
        var taskDoneAdapter: TaskDoneAdapter = TaskDoneAdapter(taskDoneList)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialise notre base de données
        val db = DatabaseHandler.DbReaderHelper(this)
        Log.d("baseDone", db.getAllTasksDone().size.toString())
        Log.d("baseNotDone", db.getAllTasksNotDone().size.toString())

        // Initialise nos listes de tâches
        for (i in 0 until db.getAllTasksNotDone().size) {
            taskNotDoneList.add(db.getTaskNotDone(i + 1))
        }

        for (i in 0 until db.getAllTasksDone().size) {
            taskDoneList.add(db.getTaskDone(i + 1))
        }

        // Initialise nos deux RecyclerView
        val recyclerViewDone: RecyclerView = findViewById(R.id.listOfTaskDone)
        recyclerViewDone.layoutManager = LinearLayoutManager(this)
        recyclerViewDone.adapter = taskDoneAdapter

        val recyclerViewNotDone: RecyclerView = findViewById(R.id.listOfTaskNotDone)
        recyclerViewNotDone.layoutManager = LinearLayoutManager(this)
        recyclerViewNotDone.adapter = taskNotDoneAdapter

        // Action du bouton d'ajout de tâche
        addTaskButton = findViewById(R.id.btnAddTaskPage)
        addTaskButton.setOnClickListener(View.OnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        })

        // Action du bouton Done
        doneTaskButton = findViewById(R.id.btnDone)
        doneTaskButton.setOnClickListener(View.OnClickListener {
            if (doneTaskButton.text == "Not Done") {
                recyclerViewNotDone.visibility = View.GONE
                recyclerViewDone.visibility = View.VISIBLE
                doneTaskButton.text = "Done"
                doneTaskButton.setBackgroundColor(resources.getColor(R.color.white))
                doneTaskButton.setTextColor(resources.getColor(R.color.black))
            } else {
                recyclerViewNotDone.visibility = View.VISIBLE
                recyclerViewDone.visibility = View.GONE
                doneTaskButton.text = "Not Done"
                doneTaskButton.setBackgroundColor(resources.getColor(R.color.blue))
                doneTaskButton.setTextColor(resources.getColor(R.color.white))
            }
        })
    }

    //----------------------------------------------------------------------------------------------


}