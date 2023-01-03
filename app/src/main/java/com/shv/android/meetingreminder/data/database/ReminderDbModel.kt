package com.shv.android.meetingreminder.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shv.android.meetingreminder.domain.entity.Client

@Entity(tableName = "reminders")
data class ReminderDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val date: String,
    val time: String?,
    @Embedded
    val client: Client
)
