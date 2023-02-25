package com.shv.android.meetingreminder.domain.usecase.validation_usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import javax.inject.Inject

class ValidateClientUseCase @Inject constructor(private val repository: ReminderRepository) {

    operator fun invoke(client: String) : ValidationResult {
        return repository.validateClient(client)
    }
}