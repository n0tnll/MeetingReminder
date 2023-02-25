package com.shv.android.meetingreminder.presentation

import java.util.Calendar

sealed class AddReminderFormEvent {
    data class TitleChange(val title: String): AddReminderFormEvent()
    data class ClientChange(val client: String): AddReminderFormEvent()
    data class DateChange(val date: Calendar): AddReminderFormEvent()
    data class TimeChange(val time: Calendar): AddReminderFormEvent()
}
