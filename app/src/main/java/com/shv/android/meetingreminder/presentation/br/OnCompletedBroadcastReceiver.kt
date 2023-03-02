package com.shv.android.meetingreminder.presentation.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.shv.android.meetingreminder.di.ApplicationScope
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.UpdateTaskStatusUseCase
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
class OnCompletedBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateTaskStatusUseCase: UpdateTaskStatusUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.applicationContext as MeetingReminderApplication).component.inject(this)
        val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("reminder", Reminder::class.java) as Reminder
        } else {
            intent?.getParcelableExtra("reminder") as? Reminder
        }
        Log.d("getParcelableExtra", "from onReceive -> reminderId: ${reminder?.id}")
        Log.d("getParcelableExtra", "from onReceive -> reminderTitle: ${reminder?.title}")
        Log.d(
            "getParcelableExtra",
            "from onReceive -> reminderClient: ${reminder?.client?.clientFullName}"
        )
        Log.d("getParcelableExtra", "from onReceive -> reminderDateTime: ${reminder?.dateTime}")
        if (reminder != null) {
            reminder.status = true
        }
        CoroutineScope(IO).launch {
            reminder?.let {
                updateTaskStatusUseCase(it)
            }
        }
        if (context != null && reminder != null)
            NotificationManagerCompat.from(context).cancel(null, reminder.id)
    }

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, OnCompletedBroadcastReceiver::class.java)
        }
    }
}