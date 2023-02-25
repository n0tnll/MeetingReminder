package com.shv.android.meetingreminder.domain.usecase.validation_usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import java.util.Calendar
import javax.inject.Inject

class ValidateDateUseCase @Inject constructor(private val repository: ReminderRepository) {

    operator fun invoke(date: Calendar) : ValidationResult {
        return repository.validateDate(date)
    }
}