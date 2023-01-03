package com.shv.android.meetingreminder.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY date DESC")
    fun getReminderList(): LiveData<List<ReminderDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderDbModel)

    @Query("SELECT * FROM reminders WHERE id=:reminderId LIMIT 1")
    suspend fun getReminder(reminderId: Int): ReminderDbModel

    @Query("DELETE FROM reminders WHERE id=:reminderId")
    suspend fun deleteReminder(reminderId: Int)
}