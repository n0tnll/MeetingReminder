package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder
import javax.inject.Inject

class UpdateTaskStatusUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        repository.updateTaskStatus(reminder)
    }
}