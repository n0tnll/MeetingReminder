package com.shv.android.meetingreminder.domain.entity

data class Reminder(
    val id: Int,
    val title: String,
    val date: String,
    val time: String?,
    val client: Client
)
