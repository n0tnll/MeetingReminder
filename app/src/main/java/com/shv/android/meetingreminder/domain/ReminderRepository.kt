package com.shv.android.meetingreminder.domain

import androidx.lifecycle.LiveData
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.validation_usecase.ValidationResult
import java.util.*

interface ReminderRepository {

    fun getReminderList(): LiveData<List<Reminder>>

    suspend fun addReminder(reminderItem: Reminder)

    suspend fun editReminder(reminderItem: Reminder)

    suspend fun deleteReminder(reminderItem: Reminder)

    suspend fun getReminderItem(reminderItemId: Int): Reminder

    suspend fun loadClientsList(): List<Client>

    suspend fun chooseClient(clientId: Int): Client

    fun validateTitle(title: String): ValidationResult

    fun validateClient(client: String): ValidationResult

    fun validateDate(date: Calendar): ValidationResult

    fun validateTime(time: Calendar): ValidationResult

    suspend fun getActiveAlarms(time: Long): List<Reminder>
    suspend fun updateTaskStatus(reminder: Reminder)
}