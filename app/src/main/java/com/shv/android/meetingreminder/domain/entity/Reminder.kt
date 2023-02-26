package com.shv.android.meetingreminder.domain.entity

data class Reminder(
    val title: String,
    val fullName: String,
    val dateTime: Long,
    val client: Client,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
