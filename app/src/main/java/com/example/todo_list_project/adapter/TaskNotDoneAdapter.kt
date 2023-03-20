package com.example.todo_list_project.adapter

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.AddNewTask
import com.example.todo_list_project.MainActivity
import com.example.todo_list_project.R
import com.example.todo_list_project.classes.Task
import com.example.todo_list_project.handler.DatabaseHandler
import java.util.*

class TaskNotDoneAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskNotDoneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_not_done_item, parent, false)
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
        private val title: TextView = itemView.findViewById(R.id.textTitle)
        private val description: TextView = itemView.findViewById(R.id.textDescription)
        private val startingDate: TextView = itemView.findViewById(R.id.textStartingDate)
        private val endingDate: TextView = itemView.findViewById(R.id.textEndingDate)
        private val reminder: TextView = itemView.findViewById(R.id.textReminder)
        private val validTaskIcon: ImageView = itemView.findViewById(R.id.validButton)
        private val deleteTaskIcon: ImageView = itemView.findViewById(R.id.deleteButton)
        private val stateButton: Button = itemView.findViewById(R.id.stateButton)
        private val expandedLayout: LinearLayout = itemView.findViewById(R.id.expandedLayout)
        private val success: ImageView = itemView.findViewById(R.id.success)

        fun bind(task: Task) {
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            val outputFormatTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)

            title.text = task.title
            description.text = task.description
            startingDate.text = outputFormat.format(task.startingDate)
            endingDate.text = outputFormat.format(task.endingDate)
            reminder.text = outputFormatTime.format(task.reminder)

            validTaskIcon.setOnClickListener {
                val handler = Handler()
                handler.postDelayed({
                    val db = DatabaseHandler.DbReaderHelper(itemView.context)
                    task.id.let { it1 -> db.updateTaskToDone(it1) }
                    db.close()
                    MainActivity.taskNotDoneList.remove(task)
                    MainActivity.taskNotDoneAdapter.notifyItemRemoved(adapterPosition)
                    MainActivity.taskDoneList.add(task)
                    MainActivity.taskDoneAdapter.notifyItemInserted(MainActivity.taskDoneList.size - 1)
                }, 2800)

                success.visibility = View.VISIBLE
                expandedLayout.visibility = View.INVISIBLE
                success.animate().apply {
                    duration = 3000
                    rotationYBy(720f)
                }.withEndAction {
                    success.visibility = View.INVISIBLE
                    expandedLayout.visibility = View.VISIBLE
                }
            }

            deleteTaskIcon.setOnClickListener {
                val db = DatabaseHandler.DbReaderHelper(itemView.context)
                task.id.let { it1 -> db.deleteTask(it1) }
                Log.d("delete", db.getAllTasks().size.toString())
                db.close()
                MainActivity.taskNotDoneList.remove(task)
                MainActivity.taskNotDoneAdapter.notifyItemRemoved(adapterPosition)

                AddNewTask.cancelAlarm(itemView.context, task.id)
                AddNewTask.cancelAlarm(itemView.context, task.id+100)
            }

            if (task.isLate) {
                Log.d("late", "late")
                stateButton.setBackgroundColor(Color.RED)
            } else {
                stateButton.setBackgroundColor(Color.GREEN)
            }
        }
    }
}