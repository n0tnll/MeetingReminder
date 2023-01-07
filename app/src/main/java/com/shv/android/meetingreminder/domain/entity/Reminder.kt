package com.shv.android.meetingreminder.domain.entity

data class Reminder(
    val title: String,
    val date: String,
    val time: String?,
    val client: Client,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
