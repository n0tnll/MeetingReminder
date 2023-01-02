package com.shv.android.meetingreminder.domain.entity

import java.util.Date

data class Reminder(
    val id: Int,
    val title: String,
    val date: Date,
    val time: Date?,
    val client: Client
)
