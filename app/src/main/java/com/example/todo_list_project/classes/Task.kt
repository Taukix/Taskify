package com.example.todo_list_project.classes

import java.util.Date

class Task(
    val id: Int,
    val title: String,
    val description: String,
    val startingDate: Date?,
    val endingDate: Date?,
    val reminder: Date?,
    val isDone: Int,
    var isLate: Boolean = false) {
}