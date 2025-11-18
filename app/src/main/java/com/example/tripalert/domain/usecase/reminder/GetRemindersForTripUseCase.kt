package com.example.tripalert.domain.usecase.reminder

import com.example.tripalert.domain.models.Reminder
import com.example.tripalert.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class GetRemindersForTripUseCase(private val repository: ReminderRepository) {
    operator fun invoke(tripId: Long): Flow<List<Reminder>> =
        repository.getRemindersForTrip(tripId.toLong())

}
