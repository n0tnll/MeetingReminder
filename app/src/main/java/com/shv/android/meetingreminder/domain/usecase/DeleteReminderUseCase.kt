package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder

class DeleteReminderUseCase(private val repository: ReminderRepository) {

    suspend operator fun invoke(reminderItem: Reminder) {
        repository.deleteReminder(reminderItem)
    }
}