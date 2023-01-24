package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(reminderItem: Reminder) {
        repository.addReminder(reminderItem)
    }
}