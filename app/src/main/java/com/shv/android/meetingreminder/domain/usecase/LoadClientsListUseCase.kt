package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository

class LoadClientsListUseCase(private val repository: ReminderRepository) {

    suspend operator fun invoke() {
        repository.loadClientsList()
    }
}