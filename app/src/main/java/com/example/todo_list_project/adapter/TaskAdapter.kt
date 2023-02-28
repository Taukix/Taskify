import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list_project.R
import com.example.todo_list_project.classes.Task

class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleLayout: LinearLayout = itemView.findViewById(R.id.titleLayout)
        private val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
        private val detailsLayout: LinearLayout = itemView.findViewById(R.id.expandedLayout)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textDescription)
        private val startingDateTextView: TextView = itemView.findViewById(R.id.textStartingDate)
        private val endingDateTextView: TextView = itemView.findViewById(R.id.textEndingDate)
        private val reminderTextView: TextView = itemView.findViewById(R.id.textReminder)

        private val collapsedHeight: Int = itemView.resources.getDimensionPixelSize(R.dimen.task_item_collapsed_height)
        private val expandedHeight: Int = itemView.resources.getDimensionPixelSize(R.dimen.task_item_expanded_height)
        private val duration: Long = itemView.resources.getInteger(R.integer.animation_duration).toLong()

        private var isExpanded: Boolean = false

        init {
            titleLayout.setOnClickListener {
                toggleExpansion()
            }
        }

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            startingDateTextView.text = task.startingDate.toString()
            endingDateTextView.text = task.endingDate.toString()
            reminderTextView.text = task.reminder.toString()

            titleLayout.visibility = View.VISIBLE
            detailsLayout.visibility = View.GONE
        }

        private fun toggleExpansion() {
            val startHeight = if (isExpanded) expandedHeight else collapsedHeight
            val endHeight = if (isExpanded) collapsedHeight else expandedHeight

            val animator = ValueAnimator.ofInt(startHeight, endHeight)
            animator.duration = duration

            animator.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                val layoutParams = detailsLayout.layoutParams
                layoutParams.height = animatedValue
                detailsLayout.layoutParams = layoutParams
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animation != null) {
                        super.onAnimationEnd(animation)
                    }
                    isExpanded = !isExpanded

                    if (isExpanded) {
                        titleLayout.visibility = View.GONE
                        detailsLayout.visibility = View.VISIBLE
                    } else {
                        titleLayout.visibility = View.VISIBLE
                        detailsLayout.visibility = View.GONE
                    }
                }
            })

            animator.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }
}
