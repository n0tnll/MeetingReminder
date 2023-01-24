package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client
import javax.inject.Inject

class LoadClientsListUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(): List<Client> {
        return repository.loadClientsList()
    }
}