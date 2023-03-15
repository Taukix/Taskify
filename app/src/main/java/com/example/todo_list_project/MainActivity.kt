package com.example.todo_list_project

import TaskAdapter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.classes.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.penguincoders.doit.Utils.DatabaseHandler


class MainActivity : AppCompatActivity() {
    private lateinit var addTaskButton: ImageView

    companion object {
        var taskList = mutableListOf<Task>()
        var taskAdapter: TaskAdapter = TaskAdapter(taskList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialise notre base de données
        val db = DatabaseHandler.DbReaderHelper(this)

        // Initialise notre liste de tâches
        for (i in 0 until db.getAllTask().size) {
            taskList.add(db.getAllTask()[i])
        }

        // Initialise notre RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.listOfTask)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Action du bouton d'ajout de tâche
        addTaskButton = findViewById(R.id.btnAddTaskPage)
        addTaskButton.setOnClickListener(View.OnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        })

    }

    //----------------------------------------------------------------------------------------------
}