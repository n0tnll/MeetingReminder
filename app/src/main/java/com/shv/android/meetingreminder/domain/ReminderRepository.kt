package com.shv.android.meetingreminder.domain

import androidx.lifecycle.LiveData
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder

interface ReminderRepository {

    fun getReminderList(): LiveData<List<Reminder>>

    suspend fun addReminder(reminderItem: Reminder)

    suspend fun editReminder(reminderItem: Reminder)

    suspend fun deleteReminder(reminderItem: Reminder)

    suspend fun getReminderItem(reminderItemId: Int): Reminder

    suspend fun loadClientsList()

    suspend fun chooseClient(clientId: Int): Client
}