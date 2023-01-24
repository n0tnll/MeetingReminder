package com.shv.android.meetingreminder.domain.usecase

import androidx.lifecycle.LiveData
import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Reminder
import javax.inject.Inject

class GetReminderListUseCase @Inject constructor(private val repository: ReminderRepository) {

    operator fun invoke(): LiveData<List<Reminder>> {
        return repository.getReminderList()
    }
}