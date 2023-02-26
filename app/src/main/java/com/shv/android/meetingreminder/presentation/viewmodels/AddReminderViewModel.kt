package com.shv.android.meetingreminder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.AddReminderUseCase
import com.shv.android.meetingreminder.domain.usecase.validation_usecase.*
import com.shv.android.meetingreminder.presentation.AddReminderFormEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminderUseCase,
    private val validateTitleUseCase: ValidateTitleUseCase,
    private val validateClientUseCase: ValidateClientUseCase,
    private val validateDateUseCase: ValidateDateUseCase,
    private val validateTimeUseCase: ValidateTimeUseCase
) : ViewModel() {

    private val _state = MutableLiveData<AddReminderState>()
    val state: LiveData<AddReminderState>
        get() = _state

    init {
        _state.value = AddReminderState()
    }

    private val _client = MutableLiveData<Client>()
    val client: LiveData<Client>
        get() = _client

    fun addReminder(title: String, client: Client, dateTime: Long) {
        val reminder = Reminder(
            title = title,
            fullName = client.fullName,
            dateTime = dateTime,
            client = client
        )
        viewModelScope.launch {
            addReminderUseCase(reminder)
            finishWork()
        }
    }

    fun setClient(client: Client) {
        _client.value = client
    }

    fun onEvent(event: AddReminderFormEvent) {
        when (event) {
            is AddReminderFormEvent.TitleChange -> {
                _state.value = _state.value?.copy(title = event.title)
            }
            is AddReminderFormEvent.ClientChange -> {
                _state.value = _state.value?.copy(client = event.client)
            }
            is AddReminderFormEvent.DateChange -> {
                _state.value = _state.value?.copy(date = event.date)
            }
            is AddReminderFormEvent.TimeChange -> {
                _state.value = _state.value?.copy(time = event.time)
            }
        }
        submitData()
    }

    private fun submitData() {
        val titleResult = validateTitleUseCase(_state.value?.title ?: "")
        val clientResult = validateClientUseCase(_state.value?.client ?: "")
        val dateResult = validateDateUseCase(_state.value!!.date)
        val timeResult =
            if (_state.value?.time == null)
                ValidationResult(successful = true)
            else validateTimeUseCase(
                _state.value?.time
            )

        val hasError = listOf(
            titleResult,
            clientResult,
            dateResult,
            timeResult
        ).any { !it!!.successful }

        _state.value = _state.value?.copy(
            titleError = titleResult.errorMessage,
            clientError = clientResult.errorMessage,
            dateError = dateResult.errorMessage,
            timeError = timeResult?.errorMessage,
            formValid = false
        )
        if (hasError) return
        _state.value = _state.value?.copy(formValid = true)
    }

    private fun finishWork() {
        _state.value = _state.value?.copy(isFinished = true)
    }
}