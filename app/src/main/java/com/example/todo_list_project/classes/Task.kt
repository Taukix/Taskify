package com.example.todo_list_project.classes

import java.util.Date

class Task(
    var id: Int?,
    var title: String,
    var description: String,
    var startingDate: Date?,
    var endingDate: Date?,
    var reminder: Date?) {
}