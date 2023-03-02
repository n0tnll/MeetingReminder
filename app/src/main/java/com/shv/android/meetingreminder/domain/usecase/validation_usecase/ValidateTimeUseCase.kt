package com.shv.android.meetingreminder.domain.usecase.validation_usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import java.util.Calendar
import javax.inject.Inject

class ValidateTimeUseCase @Inject constructor(private val repository: ReminderRepository) {

    operator fun invoke(calendar: Calendar) : ValidationResult {
        return repository.validateTime(calendar)
    }
}