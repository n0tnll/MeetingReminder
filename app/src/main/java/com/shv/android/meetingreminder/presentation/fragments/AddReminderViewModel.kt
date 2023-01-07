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

class AddReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReminderRepositoryImpl(application)
    private val addReminderUseCase = AddReminderUseCase(repository)

    private val _errorInputTitle = MutableLiveData<Boolean>()
    val errorInputTitle: LiveData<Boolean>
        get() = _errorInputTitle

    private val _isCompleted = MutableLiveData<Boolean>()
    val isCompleted: LiveData<Boolean>
        get() = _isCompleted

    fun addReminder(title: String, client: Client, date: String) {
        //TODO: add validation
        val reminder = Reminder(
            title = title,
            client = client,
            date = date,
            time = "test"
        )
        viewModelScope.launch {
            addReminderUseCase(reminder)
            finishWork()
        }
    }

    private fun finishWork() {
        _isCompleted.value = true
    }
}