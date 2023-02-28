package com.example.todo_list_project.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.R
import com.example.todo_list_project.classes.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val startingDateTextView: TextView = view.findViewById(R.id.startingDateTextView)
        val endingDateTextView: TextView = view.findViewById(R.id.endingDateTextView)
        val reminderDateTextView: TextView = view.findViewById(R.id.reminderDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.titleTextView.text = task.title
        holder.descriptionTextView.text = task.description
        holder.startingDateTextView.text = formatDate(task.startingDate)
        holder.endingDateTextView.text = formatDate(task.endingDate)
        holder.reminderDateTextView.text = formatDateTime(task.reminder)

        // Arrondir les bords du CardView
        holder.cardView.radius = 16f
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun formatDateTime(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }
}