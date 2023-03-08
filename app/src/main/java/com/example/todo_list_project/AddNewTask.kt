package com.example.todo_list_project

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask : BottomSheetDialogFragment() {

    private lateinit var buttonStartingDate: Button
    private lateinit var buttonEndingDate: Button
    private lateinit var buttonReminderDate: Button
    private lateinit var buttonReminderTime: Button

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View =
            inflater.inflate(R.layout.add_task, container, false)
        dialog?.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        buttonStartingDate = view.findViewById(R.id.buttonStartingDate)
        buttonStartingDate.setOnClickListener{ showDatePicker(it, buttonStartingDate) }
        buttonEndingDate = view.findViewById(R.id.buttonEndingDate)
        buttonEndingDate.setOnClickListener{ showDatePicker(it, buttonEndingDate) }
        buttonReminderDate = view.findViewById(R.id.buttonReminderDate)
        buttonReminderDate.setOnClickListener{ showDatePicker(it, buttonReminderDate) }
        buttonReminderTime = view.findViewById(R.id.buttonReminderTime)
        buttonReminderTime.setOnClickListener{ showTimePicker(it, buttonReminderTime) }
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