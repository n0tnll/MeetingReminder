package com.shv.android.meetingreminder.presentation

import android.app.Application
import com.shv.android.meetingreminder.di.DaggerApplicationComponent

class MeetingReminderApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}