import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.MainActivity
import com.example.todo_list_project.R
import com.example.todo_list_project.handler.classes.Task

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
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

        fun bind(task: Task) {
            title.text = task.title
            description.text = task.description
            startingDate.text = task.startingDate.toString()
            endingDate.text = task.endingDate.toString()
            reminder.text = task.reminder.toString()
        }
    }
}