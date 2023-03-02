package com.shv.android.meetingreminder.data.mapper

import com.shv.android.meetingreminder.data.database.ReminderDbModel
import com.shv.android.meetingreminder.data.network.model.ClientDto
import com.shv.android.meetingreminder.data.network.model.ClientsListDto
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ReminderMapper @Inject constructor() {

    fun mapDbModelToEntity(dbModel: ReminderDbModel) = Reminder(
        id = dbModel.id,
        fullName = dbModel.client.clientFullName,
        title = dbModel.title,
        status = dbModel.status,
        dateTime = dbModel.dateTime,
        client = dbModel.client
    )

    fun mapEntityToDbModel(reminder: Reminder) = ReminderDbModel(
        id = reminder.id,
        title = reminder.title,
        status = reminder.status,
        dateTime = reminder.dateTime,
        client = reminder.client
    )

    private fun mapClientDtoToEntity(clientDto: ClientDto): Client {
        return Client(
            clientId = atomicInteger.addAndGet(1),
            clientFullName = mapFullName(
                clientDto.clientNameDto.title,
                clientDto.clientNameDto.first,
                clientDto.clientNameDto.last
            ),
            email = clientDto.email,
            imgUrl = clientDto.clientPictures.large
        )
    }

    private fun mapFullName(title: String, first: String, last: String): String {
        return "$title $first $last"
    }

    fun mapListDtoToListEntity(listDto: ClientsListDto) = listDto.clients.map {
        mapClientDtoToEntity(it)
    }

    fun mapListReminderDbModelToListEntity(listDbModel: List<ReminderDbModel>): List<Reminder> {
        val list = listDbModel.map {
            mapDbModelToEntity(it)
        }
        return list
    }

    companion object {
        private val atomicInteger = AtomicInteger(0)
    }
}