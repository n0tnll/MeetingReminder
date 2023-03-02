package com.shv.android.meetingreminder.presentation.br

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.GetActiveAlarmsUseCase
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class RebootBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getActiveAlarmsUseCase: GetActiveAlarmsUseCase

    override fun onReceive(context: Context, intent: Intent?) {
        (context.applicationContext as MeetingReminderApplication).component.inject(this)
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val time = Calendar.getInstance().timeInMillis
            CoroutineScope(Dispatchers.Main).launch {
                val list = getActiveAlarmsUseCase(time)
                for (reminder in list) setAlarm(reminder, context)
            }
        }
    }

    private fun setAlarm(reminder: Reminder, context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(context).apply {
            putExtra("reminder", reminder)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.dateTime, pendingIntent)
    }
}