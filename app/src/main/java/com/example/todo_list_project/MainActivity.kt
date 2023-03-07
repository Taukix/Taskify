package com.example.todo_list_project

import TaskAdapter
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.handler.classes.Task
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialise notre liste de tâches
        taskList = getTaskList()

        // Initialise notre RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.listOfTask)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter
    }

    // Gestion de la liste de Tâches

    private fun getTaskList(): List<Task> {
        // Création d'une liste de tâches factice pour cet exemple
        val taskList = mutableListOf<Task>()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        taskList.add(
            Task("Tâche 1", "Description de la tâche 1",
            dateFormat.parse("01/01/2022"), dateFormat.parse("31/01/2022"),
            dateFormat.parse("15/01/2022 12:00"))
        )

        taskList.add(
            Task("Tâche 2", "Description de la tâche 2",
            dateFormat.parse("01/02/2022"), dateFormat.parse("28/02/2022"),
            dateFormat.parse("15/02/2022 12:00"))
        )

        taskList.add(
            Task("Tâche 3", "Description de la tâche 3",
            dateFormat.parse("01/03/2022"), dateFormat.parse("31/03/2022"),
            dateFormat.parse("15/03/2022 12:00"))
        )

        taskList.add(
            Task("Tâche 4", "Description de la tâche 4",
            dateFormat.parse("01/04/2022"), dateFormat.parse("30/04/2022"),
            dateFormat.parse("15/04/2022 12:00"))
        )

        taskList.add(
            Task("Tâche 5", "Description de la tâche 5",
            dateFormat.parse("01/05/2022"), dateFormat.parse("31/05/2022"),
            dateFormat.parse("15/05/2022 12:00"))
        )

        return taskList
    }

    //----------------------------------------------------------------------------------------------
}