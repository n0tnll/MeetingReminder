package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder

class GetReminderItemUseCase(private val repository: ReminderRepository) {

    suspend operator fun invoke(reminderItemId: Int): Reminder {
        return repository.getReminderItem(reminderItemId)
    }
}