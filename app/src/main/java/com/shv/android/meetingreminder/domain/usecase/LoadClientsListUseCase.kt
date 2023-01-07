package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client

class LoadClientsListUseCase(private val repository: ReminderRepository) {

    suspend operator fun invoke(): List<Client> {
        return repository.loadClientsList()
    }
}