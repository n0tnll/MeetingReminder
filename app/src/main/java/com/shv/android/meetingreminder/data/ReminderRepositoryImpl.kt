package com.shv.android.meetingreminder.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.data.database.ReminderDao
import com.shv.android.meetingreminder.data.mapper.ReminderMapper
import com.shv.android.meetingreminder.data.network.api.ApiService
import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.validation_usecase.ValidationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val application: Application,
    private val reminderDao: ReminderDao,
    private val apiService: ApiService,
    private val mapper: ReminderMapper
) : ReminderRepository {

    private var today = false

    override fun getReminderList(): LiveData<List<Reminder>> {
        return Transformations.map(reminderDao.getReminderList()) {
            it.map { dbModel ->
                mapper.mapDbModelToEntity(dbModel)
            }
        }
    }

    override suspend fun addReminder(reminderItem: Reminder) {
        reminderDao.addReminder(mapper.mapEntityToDbModel(reminderItem))
    }

    override suspend fun getReminderItem(reminderItemId: Int): Reminder {
        val dbModel = reminderDao.getReminder(reminderItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun editReminder(reminderItem: Reminder) {
        reminderDao.addReminder(mapper.mapEntityToDbModel(reminderItem))
    }

    override suspend fun deleteReminder(reminderItem: Reminder) {
        CoroutineScope(IO).launch {
            reminderDao.deleteReminder(reminderItem.id)
        }
    }

    override suspend fun chooseClient(clientId: Int): Client {
        TODO("Not yet implemented")
    }

    override suspend fun loadClientsList(): List<Client> {
        val clients = apiService.getContacts()
        return mapper.mapListDtoToListEntity(clients)
    }

    override fun validateTitle(title: String): ValidationResult {
        if (title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = application.applicationContext.getString(R.string.empty_field_error)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    override fun validateClient(client: String): ValidationResult {
        if (client.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = application.applicationContext.getString(R.string.empty_field_error)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    override fun validateDate(date: Calendar): ValidationResult {
        val dateNow = LocalDate.now()
        val meetingDate =
            LocalDateTime.ofInstant(date.toInstant(), date.timeZone.toZoneId()).toLocalDate()

        today = meetingDate == dateNow

        if (meetingDate < dateNow) {
            today = false
            return ValidationResult(
                successful = false,
                errorMessage = application.applicationContext.getString(R.string.incorrect_date)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    override fun validateTime(time: Calendar?): ValidationResult {
        if (time == null) {
            return ValidationResult(
                successful = true
            )
        }
        val timeNow = LocalTime.now()
        val meetingTime = LocalTime.of(time[Calendar.HOUR_OF_DAY], time[Calendar.MINUTE])
        if (today) {
            if (meetingTime <= timeNow)
                return ValidationResult(
                    successful = false,
                    errorMessage = application.applicationContext.getString(R.string.incorrect_time)
                )
        }
        return ValidationResult(
            successful = true
        )
    }
}