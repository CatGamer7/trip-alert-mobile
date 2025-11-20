package com.example.tripalert.data.mapper

import com.example.tripalert.data.remote.dto.CreateReminderDTO
import com.example.tripalert.data.remote.dto.ReminderResponseDTO
import com.example.tripalert.data.remote.dto.UpdateReminderDTO
import com.example.tripalert.domain.models.Reminder
import com.example.tripalert.domain.models.Trip

object ReminderMapper {

    fun fromDto(dto: ReminderResponseDTO, trip: Trip): Reminder = Reminder(
        id = dto.id,
        trip = trip,
        notificationTime = dto.notificationTime,
        sent = dto.sent
    )

    fun toCreateDto(reminder: Reminder): CreateReminderDTO = CreateReminderDTO(
        notificationTime = reminder.notificationTime
    )

    fun toUpdateDto(reminder: Reminder): UpdateReminderDTO = UpdateReminderDTO(
        notificationTime = reminder.notificationTime,
        sent = reminder.sent
    )
}
