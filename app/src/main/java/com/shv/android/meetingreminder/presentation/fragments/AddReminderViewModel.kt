package com.shv.android.meetingreminder.presentation.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.data.ReminderRepositoryImpl
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.AddReminderUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReminderRepositoryImpl(application)
    private val addReminderUseCase = AddReminderUseCase(repository)

    private val _errorInputTitle = MutableLiveData(true)
    val errorInputTitle: LiveData<Boolean>
        get() = _errorInputTitle

    private val _client = MutableLiveData<Client?>()
    val client: LiveData<Client?>
        get() = _client

    private val _errorClientField = MutableLiveData(true)
    val errorClientField: LiveData<Boolean>
        get() = _errorClientField

    private val _errorDate = MutableLiveData(true)
    val errorDate: LiveData<Boolean>
        get() = _errorDate

    private val _errorTime = MutableLiveData(false)
    val errorTime: LiveData<Boolean>
        get() = _errorTime

    private val _reminderValid = MutableLiveData(false)
    val reminderValid: LiveData<Boolean>
        get() = _reminderValid

    private val _isCompleted = MutableLiveData<Boolean>()
    val isCompleted: LiveData<Boolean>
        get() = _isCompleted

    fun addReminder(title: String, client: Client, date: String, time: String) {
        val reminder = Reminder(
            title = title,
            fullName = client.fullName,
            date = date,
            time = time,
            client = client
        )
        viewModelScope.launch {
            addReminderUseCase(reminder)
            finishWork()
        }
    }

    fun setClient(client: Client?) {
        _client.value = client
    }

    private fun isValidReminder() {
        _reminderValid.value = _errorInputTitle.value == false
                && _errorClientField.value == false
                && _errorDate.value == false && _errorTime.value == false
    }

    fun validateTitle(title: String) {
        _errorInputTitle.value = title.isBlank()
        isValidReminder()
    }

    fun validateClient(client: String) {
        _errorClientField.value = client.isBlank()
        isValidReminder()
    }

    fun validateDate(dateFromEditText: String) {
        val now = LocalDate.now()
        val meetingDate = localDateFromString(dateFromEditText)

        _errorDate.value = meetingDate < now
        isValidReminder()
    }

    fun validateTime(dateFromEditText: String, timeFromEditText: String) {
        if (timeFromEditText.isNotBlank()) {
            val now = LocalDateTime.now()
            val meetingDate = localDateFromString(dateFromEditText)
            val meetingTime = localTimeFromString(timeFromEditText)

            _errorTime.value = if (meetingDate <= now.toLocalDate()) {
                meetingTime < now.toLocalTime()
            } else false
            isValidReminder()
        } else {
            _errorTime.value = false
            isValidReminder()
        }
    }

    private fun localDateFromString(dateString: String): LocalDate {
        if (dateString.isNotBlank()) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
            return LocalDate.parse(dateString, dateFormatter)
        } else return LocalDate.now()
    }

    private fun localTimeFromString(timeString: String): LocalTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        return LocalTime.parse(timeString, formatter)
    }

    private fun finishWork() {
        _isCompleted.value = true
    }
}