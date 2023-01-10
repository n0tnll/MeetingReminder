package com.shv.android.meetingreminder.presentation.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.data.ReminderRepositoryImpl
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.DeleteReminderUseCase
import com.shv.android.meetingreminder.domain.usecase.GetReminderListUseCase
import kotlinx.coroutines.launch

class ReminderListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReminderRepositoryImpl(application)

    private val getReminderListUseCase = GetReminderListUseCase(repository)
    private val deleteReminderUseCase = DeleteReminderUseCase(repository)

    val reminderList = getReminderListUseCase()

    fun deleteReminderItem(reminder: Reminder) {
        viewModelScope.launch {
            deleteReminderUseCase(reminder)
        }
    }
}