package com.shv.android.meetingreminder.presentation.br

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.domain.entity.Reminder

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("reminder", Reminder::class.java) as Reminder
        } else {
            intent?.getParcelableExtra("reminder") as? Reminder
        }
        Log.d("AlarmReceiver", "from onReceive -> reminderId: ${reminder?.id}")
        Log.d("AlarmReceiver", "from onReceive -> reminderTitle: ${reminder?.title}")
        Log.d("AlarmReceiver", "from onReceive -> reminderClient: ${reminder?.client?.clientFullName}")
        Log.d("AlarmReceiver", "from onReceive -> reminderDateTime: ${reminder?.dateTime}")

        val notificationManager =
            context?.let {
                getSystemService(it , NotificationManager::class.java) as NotificationManager
            }
        if (notificationManager != null) {
            createNotificationChannel(notificationManager)
        }

        val intentCompleted = OnCompletedBroadcastReceiver.newIntent(context).apply {
            putExtra("reminder", reminder)
        }
        val pendingIntentCompleted = reminder?.let {
            PendingIntent.getBroadcast(
                context,
                it.id,
                intentCompleted,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val actionCompleted = NotificationCompat.Action.Builder(
            0,
            "Completed",
            pendingIntentCompleted
        ).build()

        val notification = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(reminder?.title)
                .setContentText("Ваша встреча с ${reminder?.fullName} через 1 час")
                .setSmallIcon(R.drawable.baseline_people_24)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntentCompleted)
                .addAction(actionCompleted)
                .build()
        }

        reminder?.id?.let { notificationManager?.notify(it, notification) }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Напоминание о встрече с клиентом"
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Meeting reminder"

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}