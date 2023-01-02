package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder

class AddReminderUseCase(private val repository: ReminderRepository) {

    operator fun invoke(reminderItem: Reminder) {
        repository.addReminder(reminderItem)
    }
}