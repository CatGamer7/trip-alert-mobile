package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.ReminderMapper
import com.example.tripalert.data.remote.api.ReminderApi
import com.example.tripalert.domain.models.Reminder
import com.example.tripalert.domain.models.Trip
import com.example.tripalert.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(private val api: ReminderApi) : ReminderRepository {

    private val reminders = MutableStateFlow<List<Reminder>>(emptyList())

    override suspend fun createReminder(reminder: Reminder) {
        val response = api.createReminder(ReminderMapper.toCreateDto(reminder))
        reminders.value += ReminderMapper.fromDto(response, reminder.trip)
    }

    override suspend fun updateReminder(reminder: Reminder) {
        val response = api.updateReminder(reminder.id, ReminderMapper.toUpdateDto(reminder))
        reminders.value = reminders.value.map {
            if (it.id == reminder.id) ReminderMapper.fromDto(response, reminder.trip) else it
        }
    }

    override suspend fun deleteReminder(reminderId: Long) {
        api.deleteReminder(reminderId)
        reminders.value = reminders.value.filter { it.id != reminderId }
    }

    override fun getRemindersForTrip(tripId: Long): Flow<List<Reminder>> =
        reminders.map { it.filter { r -> r.trip.id == tripId } }
}