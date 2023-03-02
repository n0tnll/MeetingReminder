package com.shv.android.meetingreminder.domain.entity

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize
import java.util.concurrent.atomic.AtomicInteger

@Parcelize
data class Reminder(
    val title: String,
    val fullName: String,
    var status: Boolean,
    val dateTime: Long,
    @Embedded
    val client: Client,
    val id: Int = atomicInteger.addAndGet(1)
) : Parcelable {
    companion object {
        private val atomicInteger = AtomicInteger(0)
    }
}
