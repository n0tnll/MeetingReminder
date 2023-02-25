package com.shv.android.meetingreminder.presentation.viewmodels

import java.util.Calendar

data class AddReminderState(
    val title: String = "",
    val titleError: String? = null,
    val client: String = "",
    val clientError: String? = null,
    val date: Calendar = Calendar.getInstance(),
    val dateError: String? = null,
    val time: Calendar? = null,
    val timeError: String? = null,
    val formValid: Boolean = false,
    val isFinished: Boolean = false
)
