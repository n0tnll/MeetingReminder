package com.shv.android.meetingreminder.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.shv.android.meetingreminder.data.database.ReminderDao
import com.shv.android.meetingreminder.data.mapper.ReminderMapper
import com.shv.android.meetingreminder.data.network.api.ApiService
import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val apiService: ApiService,
    private val mapper: ReminderMapper
) : ReminderRepository {

    override fun getReminderList(): LiveData<List<Reminder>> {
        return Transformations.map(reminderDao.getReminderList()) {
            it.map { dbModel ->
                mapper.mapDbModelToEntity(dbModel)
            }
        }
    }

    override suspend fun addReminder(reminderItem: Reminder) {
        reminderDao.addReminder(mapper.mapEntityToDbModel(reminderItem))
    }

    override suspend fun getReminderItem(reminderItemId: Int): Reminder {
        val dbModel = reminderDao.getReminder(reminderItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun editReminder(reminderItem: Reminder) {
        reminderDao.addReminder(mapper.mapEntityToDbModel(reminderItem))
    }

    override suspend fun deleteReminder(reminderItem: Reminder) {
        CoroutineScope(IO).launch {
            reminderDao.deleteReminder(reminderItem.id)
        }
    }

    override suspend fun chooseClient(clientId: Int): Client {
        TODO("Not yet implemented")
    }

    override suspend fun loadClientsList(): List<Client> {
        val result = mutableListOf<Client>()
        var isLoadingCompleted = false
        while (!isLoadingCompleted) {
            try {
                val clients = apiService.getContacts()
                val clientsListEntity = mapper.mapListDtoToListEntity(clients)
                result.addAll(clientsListEntity)
                isLoadingCompleted = true
            } catch (e: Exception) {
                Log.e("ReminderRepositoryImpl", "Проблема с загрузкой: ${e.message}")
                delay(10000)
            }
        }
        return result
    }
}