package com.shv.android.meetingreminder.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReminderDao {

    @Query(
        "SELECT * " +
                "FROM reminders " +
                "ORDER BY status " +
                "AND dateTime"
    )
    fun getReminderList(): LiveData<List<ReminderDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminder: ReminderDbModel)

    @Query(
        "SELECT * " +
                "FROM reminders " +
                "WHERE id=:reminderId LIMIT 1"
    )
    suspend fun getReminder(reminderId: Int): ReminderDbModel

    @Query(
        "DELETE FROM reminders " +
                "WHERE id=:reminderId"
    )
    suspend fun deleteReminder(reminderId: Int)

    @Query(
        "SELECT * " +
                "FROM reminders " +
                "WHERE status = 0 " +
                "AND dateTime > :currentTime"
    )
    fun getActiveAlarms(currentTime: Long): List<ReminderDbModel>
}