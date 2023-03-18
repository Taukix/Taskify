package com.example.todo_list_project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "Alarme déclenchée")
        val title = intent.getStringExtra("title") ?: "Titre par défaut"
        val description = intent.getStringExtra("description") ?: "Description par défaut"
        val state = intent.getBooleanExtra("state", false)

        val view = View.inflate(context, R.layout.task_not_done_item, null)

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