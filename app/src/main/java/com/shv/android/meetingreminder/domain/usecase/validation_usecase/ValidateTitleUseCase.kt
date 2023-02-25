package com.shv.android.meetingreminder.domain.usecase.validation_usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import javax.inject.Inject

class ValidateTitleUseCase @Inject constructor(private val repository: ReminderRepository) {

    operator fun invoke(title: String) : ValidationResult {
        return repository.validateTitle(title)
    }
}