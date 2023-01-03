package com.shv.android.meetingreminder.data

import androidx.lifecycle.LiveData
import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder

class ReminderRepositoryImpl : ReminderRepository {
    override fun getReminderList(): LiveData<List<Reminder>> {
        TODO("Not yet implemented")
    }

    override fun addReminder(reminderItem: Reminder) {
        TODO("Not yet implemented")
    }

    override fun editReminder(reminderItem: Reminder) {
        TODO("Not yet implemented")
    }

    override fun deleteReminder(reminderItem: Reminder) {
        TODO("Not yet implemented")
    }

    override fun chooseClient(clientId: Int): Client {
        TODO("Not yet implemented")
    }
}