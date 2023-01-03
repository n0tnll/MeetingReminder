package com.shv.android.meetingreminder.domain

import androidx.lifecycle.LiveData
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder

interface ReminderRepository {

    fun getReminderList(): LiveData<List<Reminder>>

    fun addReminder(reminderItem: Reminder)

    fun editReminder(reminderItem: Reminder)

    fun deleteReminder(reminderItem: Reminder)

    fun chooseClient(clientId: Int): Client
}