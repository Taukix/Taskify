package com.example.todo_list_project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.todo_list_project.handler.DatabaseHandler

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "Alarme déclenchée")
        val taskId = intent.getIntExtra("taskId", 0)
        val state = intent.getBooleanExtra("state", false)
        var title = ""
        var description = ""

        for (task in MainActivity.taskNotDoneList) {
            if (task.id == taskId) {
                if (state) {
                    task.isLate = true
                    MainActivity.taskNotDoneAdapter.notifyItemChanged(task.id - 1)
                    title = "You are late on the task: "
                } else {
                    title = "You have a task to do: "
                }
                title += task.title
                description = task.description
            }
        }

        // Créer la notification avec le titre et la description de la tâche
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "your_channel_id"
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Nom du canal de notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}