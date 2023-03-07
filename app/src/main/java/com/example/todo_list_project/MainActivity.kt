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

    private var startingDateButton: Button = findViewById(R.id.buttonStartingDate)
    private var endingDateButton: Button = findViewById(R.id.buttonEndingDate)
    private var reminderDateButton: Button = findViewById(R.id.buttonReminderDate)
    private var reminderTimeButton: Button = findViewById(R.id.buttonReminderTime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise notre liste de tâches
        taskList = getTaskList()

        // Initialise notre RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.listOfTask)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter

        // Initialise nos dates
        initDatePicker()
        initTimePicker()
        startingDateButton.setText(getTodaysDate())
        endingDateButton.setText(getTodaysDate())
        reminderDateButton.setText(getTodaysDate())
        reminderTimeButton.setText(getTime())
    }

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

    fun getTodaysDate(): String {
        val date = Calendar.getInstance().time
        return makeDateString(date.day, date.month, date.year)
    }

    fun initDatePicker() {
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            startingDateButton.text = dateFormat.format(date.time)
            endingDateButton.text = dateFormat.format(date.time)
            reminderDateButton.text = dateFormat.format(date.time)
        }

    }

    fun makeDateString(day: Int, month: Int, year: Int): String {
        return "$day/$month/$year"
    }

    fun initTimePicker() {
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val date = Calendar.getInstance()
            date.set(Calendar.HOUR_OF_DAY, hourOfDay)
            date.set(Calendar.MINUTE, minute)

            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            reminderTimeButton.text = dateFormat.format(date.time)
        }
    }

    fun makeTimeString(hour: Int, minute: Int): String {
        return "$hour:$minute"
    }

    fun getTime(): String {
        val date = Calendar.getInstance().time
        return makeTimeString(date.hours, date.minutes)
    }
}