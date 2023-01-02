package com.shv.android.meetingreminder.domain.usecase

import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client

class ChooseClientUseCase(private val repository: ReminderRepository) {

    operator fun invoke(clientId: Int): Client {
        return repository.chooseClient(clientId)
    }
}