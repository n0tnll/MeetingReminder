package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder
import javax.inject.Inject

class GetActiveAlarmsUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(currentTime: Long): List<Reminder> {
        return repository.getActiveAlarms(currentTime)
    }
}