package com.shv.android.meetingreminder.presentation.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.shv.android.meetingreminder.di.ApplicationScope
import com.shv.android.meetingreminder.domain.entity.Reminder

@ApplicationScope
class OnRepeatBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("reminder", Reminder::class.java) as Reminder
        } else {
            intent.getParcelableExtra("reminder") as? Reminder
        }
        val flag = intent.getStringExtra("flag") ?: ""
        Log.d("OnRepeatBroadcastReceiver", "reminder: ${reminder?.title}\nflag: $flag")
    }

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, OnRepeatBroadcastReceiver::class.java)
        }
    }
}