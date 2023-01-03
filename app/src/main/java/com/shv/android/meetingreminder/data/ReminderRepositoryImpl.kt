package com.shv.android.meetingreminder.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.shv.android.meetingreminder.data.database.AppDatabase
import com.shv.android.meetingreminder.data.mapper.ReminderMapper
import com.shv.android.meetingreminder.data.network.api.ApiFactory
import com.shv.android.meetingreminder.domain.ReminderRepository
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder

class ReminderRepositoryImpl(
    private val application: Application
) : ReminderRepository {

    private val reminderDao = AppDatabase.newInstance(application).reminderDao()
    private val apiService = ApiFactory.apiService
    private val mapper = ReminderMapper()

    val listClients = mutableListOf<Client>()

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
        reminderDao.deleteReminder(reminderItem.id)
    }

    override suspend fun chooseClient(clientId: Int): Client {
        TODO("Not yet implemented")
    }

    override suspend fun loadClientsList() {
        try {
            val clients = apiService.getContacts()
            listClients.addAll(mapper.mapListDtoToListEntity(clients))
        } catch (e: Exception) {
            Log.e("ReminderRepositoryImpl", "Что-то с загрузкой ${e.message}")
        }
    }
}