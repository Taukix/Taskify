package com.example.todo_list_project

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.todo_list_project.classes.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.example.todo_list_project.handler.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*

class AddNewTask : BottomSheetDialogFragment() {

    private lateinit var buttonStartingDate: Button
    private lateinit var buttonEndingDate: Button
    private lateinit var buttonReminderDate: Button
    private lateinit var buttonReminderTime: Button
    private lateinit var validateButton: Button
    private lateinit var stateButton: Button

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun cancelAlarm(context: Context, requestCode: Int) {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.add_task, container, false)
        val taskView: View = inflater.inflate(R.layout.task_not_done_item, container, false)

        val notDoneNumber = -1
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val formatComplete = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)

        buttonStartingDate = view.findViewById(R.id.buttonStartingDate)
        buttonStartingDate.setOnClickListener{ showDatePicker(it, buttonStartingDate) }
        buttonEndingDate = view.findViewById(R.id.buttonEndingDate)
        buttonEndingDate.setOnClickListener{ showDatePicker(it, buttonEndingDate) }
        buttonReminderDate = view.findViewById(R.id.buttonReminderDate)
        buttonReminderDate.setOnClickListener{ showDatePicker(it, buttonReminderDate) }
        buttonReminderTime = view.findViewById(R.id.buttonReminderTime)
        buttonReminderTime.setOnClickListener{ showTimePicker(it, buttonReminderTime) }
        stateButton = taskView.findViewById(R.id.stateButton)
        validateButton = view.findViewById(R.id.validateButton)
        validateButton.setOnClickListener {
            val title = view.findViewById<TextInputEditText>(R.id.editInputTextTitleOfTask)
            val description = view.findViewById<TextInputEditText>(R.id.editInputDescriptionOfTask)
            val startingDateText = format.parse(buttonStartingDate.text.toString())
            val endingDateText = format.parse(buttonEndingDate.text.toString())
            val reminderDateText = formatComplete.parse(buttonReminderDate.text.toString() + " " + buttonReminderTime.text.toString())

            val db = DatabaseHandler.DbReaderHelper(requireContext())
            val task = Task(db.getAllTasks().size + 1,
                title.text.toString(),
                description.text.toString(),
                startingDateText,
                endingDateText,
                reminderDateText,
                notDoneNumber)
            db.addTask(task)
            db.close()

            scheduleNotification(endingDateText!!, task.id , true, task.id)
            Thread.sleep(1000)
            scheduleNotification(reminderDateText!!, task.id, false, task.id + 100)

            MainActivity.taskNotDoneList.add(task)
            MainActivity.taskNotDoneAdapter.notifyItemInserted(MainActivity.taskNotDoneList.size - 1)

            dismiss()
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    fun showDatePicker(view: View, button: Button) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePicker,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                button.text = "$dayOfMonth/${month + 1}/$year"
            },
            2023,
            0,
            1
        )
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun showTimePicker(view: View, button: Button) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomDatePicker,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                button.text = "$hourOfDay:$minute"
            },
            12,
            0,
            true
        )
        timePickerDialog.show()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification(endDate: Date, taskId: Int, state: Boolean, requestCode: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra("taskId", taskId)
        intent.putExtra("state", state)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, endDate.time, pendingIntent)
        Log.d(TAG, "scheduleNotification: $endDate")
        Log.d(TAG, "Time : " + Calendar.getInstance().time)
    }
}