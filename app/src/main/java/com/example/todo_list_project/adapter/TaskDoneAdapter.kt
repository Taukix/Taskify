package com.example.todo_list_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.AddNewTask
import com.example.todo_list_project.MainActivity
import com.example.todo_list_project.R
import com.example.todo_list_project.classes.Task
import com.example.todo_list_project.handler.DatabaseHandler

class TaskDoneAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskDoneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_done_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_animation)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textTitleDoneTask)
        private val backTaskIcon: ImageView = itemView.findViewById(R.id.backButton)
        private val deleteTaskIcon: ImageView = itemView.findViewById(R.id.deleteButtonDoneTask)

        fun bind(task: Task) {
            title.text = task.title

            backTaskIcon.setOnClickListener {
                val db = DatabaseHandler.DbReaderHelper(itemView.context)
                task.id.let { it1 -> db.updateTaskToNotDone(it1) }
                db.close()
                MainActivity.taskDoneList.remove(task)
                MainActivity.taskDoneAdapter.notifyItemRemoved(adapterPosition)
                MainActivity.taskNotDoneList.add(task)
                MainActivity.taskNotDoneAdapter.notifyItemInserted(MainActivity.taskNotDoneList.size - 1)
            }

            deleteTaskIcon.setOnClickListener {
                val db = DatabaseHandler.DbReaderHelper(itemView.context)
                task.id.let { it1 -> db.deleteTask(it1) }
                db.close()
                MainActivity.taskDoneList.remove(task)
                MainActivity.taskDoneAdapter.notifyItemRemoved(adapterPosition)

                AddNewTask.cancelAlarm(itemView.context, task.id)
                AddNewTask.cancelAlarm(itemView.context, task.id+100)
            }
        }
    }
}