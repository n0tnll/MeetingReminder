package com.shv.android.meetingreminder.data.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shv.android.meetingreminder.domain.entity.Client
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "reminders")
data class ReminderDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    var status: Boolean,
    val dateTime: Long,
    @Embedded
    val client: Client
) : Parcelable
