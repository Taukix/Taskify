package com.example.todo_list_project

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.todo_list_project.classes.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import net.penguincoders.doit.Utils.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*

class AddNewTask : BottomSheetDialogFragment() {

    private lateinit var buttonStartingDate: Button
    private lateinit var buttonEndingDate: Button
    private lateinit var buttonReminderDate: Button
    private lateinit var buttonReminderTime: Button
    private lateinit var validateButton: Button

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.add_task, container, false)

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
        validateButton = view.findViewById(R.id.validateButton)
        validateButton.setOnClickListener {
            val title = view.findViewById<TextInputEditText>(R.id.editInputTextTitleOfTask)
            val description = view.findViewById<TextInputEditText>(R.id.editInputDescriptionOfTask)
            val db = DatabaseHandler.DbReaderHelper(requireContext())
            val task = Task(null,
                title.text.toString(),
                description.text.toString(),
                format.parse(buttonStartingDate.text.toString()),
                format.parse(buttonEndingDate.text.toString()),
                formatComplete.parse(buttonReminderDate.text.toString() + " " + buttonReminderTime.text.toString()))
            db.addTask(task)
            db.close()
            MainActivity.taskList.add(task)
            MainActivity.taskAdapter.notifyItemInserted(MainActivity.taskList.size - 1)

            dismiss()
        }
        return view
    }

    fun showDatePicker(view: View, button: Button) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePicker,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                button.text = "$dayOfMonth/${month + 1}/$year"
            },
            2021,
            0,
            1
        )
        datePickerDialog.show()
    }

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
}