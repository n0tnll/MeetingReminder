package com.shv.android.meetingreminder.data.mapper

import com.shv.android.meetingreminder.data.database.ReminderDbModel
import com.shv.android.meetingreminder.data.network.model.ClientDto
import com.shv.android.meetingreminder.data.network.model.ClientsListDto
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder
import java.util.*
import javax.inject.Inject

class ReminderMapper @Inject constructor() {

    fun mapDbModelToEntity(dbModel: ReminderDbModel) = Reminder(
        id = dbModel.id,
        fullName = dbModel.client.fullName,
        title = dbModel.title,
        dateTime = dbModel.dateTime,
        client = dbModel.client
    )

    fun mapEntityToDbModel(reminder: Reminder) = ReminderDbModel(
        id = reminder.id,
        title = reminder.title,
        dateTime = reminder.dateTime,
        client = reminder.client
    )

    private fun mapClientDtoToEntity(clientDto: ClientDto): Client {
        return Client(
            clientId = UUID.randomUUID().toString(),
            fullName = mapFullName(
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
}