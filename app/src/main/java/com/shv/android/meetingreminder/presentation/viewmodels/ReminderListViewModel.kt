package com.shv.android.meetingreminder.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.domain.usecase.DeleteReminderUseCase
import com.shv.android.meetingreminder.domain.usecase.GetReminderListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderListViewModel @Inject constructor(
    private val getReminderListUseCase: GetReminderListUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel() {

    val reminderList = getReminderListUseCase()

    fun deleteReminderItem(reminder: Reminder) {
        viewModelScope.launch {
            deleteReminderUseCase(reminder)
        }
    }
}