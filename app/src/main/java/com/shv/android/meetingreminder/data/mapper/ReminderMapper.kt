package com.shv.android.meetingreminder.data.mapper

import com.shv.android.meetingreminder.data.database.ReminderDbModel
import com.shv.android.meetingreminder.data.network.model.ClientDto
import com.shv.android.meetingreminder.data.network.model.ClientsListDto
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.entity.Reminder

class ReminderMapper {

    fun mapDbModelToEntity(dbModel: ReminderDbModel) = Reminder(
        id = dbModel.id,
        title = dbModel.title,
        date = dbModel.date,
        time = dbModel.time,
        client = dbModel.client
    )

    fun mapEntityToDbModel(reminder: Reminder) = ReminderDbModel(
        id = reminder.id,
        title = reminder.title,
        date = reminder.date,
        time = reminder.time,
        client = reminder.client
    )

    fun mapClientDtoToEntity(clientDto: ClientDto) = Client(
        clientId = clientDto.id.value,
        titleName = clientDto.clientNameDto.title,
        firstName = clientDto.clientNameDto.first,
        lastName = clientDto.clientNameDto.last,
        email = clientDto.email,
        imgUrl = clientDto.clientPictures.medium
    )

    fun mapListDtoToListEntity(listDto: ClientsListDto) = listDto.clients.map {
        mapClientDtoToEntity(it)
    }
}