package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun createReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminderId: Long)
    fun getRemindersForTrip(tripId: Long): Flow<List<Reminder>>
}