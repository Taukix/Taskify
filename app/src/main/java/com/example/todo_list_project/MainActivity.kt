package com.example.todo_list_project

import TaskAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.classes.Task
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisez votre liste de tâches ici...
        taskList = getTaskList()

        // Initialisez votre RecyclerView ici...
        val recyclerView: RecyclerView = findViewById(R.id.listOfTask)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter
    }

    private fun getTaskList(): List<Task> {
        // Création d'une liste de tâches factice pour cet exemple
        val taskList = mutableListOf<Task>()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        taskList.add(Task("Tâche 1", "Description de la tâche 1",
            dateFormat.parse("01/01/2022"), dateFormat.parse("31/01/2022"),
            dateFormat.parse("15/01/2022 12:00")))

        taskList.add(
            Task("Tâche 2", "Description de la tâche 2",
            dateFormat.parse("01/02/2022"), dateFormat.parse("28/02/2022"),
            dateFormat.parse("15/02/2022 12:00"))
        )

        taskList.add(Task("Tâche 3", "Description de la tâche 3",
            dateFormat.parse("01/03/2022"), dateFormat.parse("31/03/2022"),
            dateFormat.parse("15/03/2022 12:00")))

        return taskList
    }
}