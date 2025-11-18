package com.example.tripalert.domain.usecase.reminder

import com.example.tripalert.domain.models.Reminder
import com.example.tripalert.domain.repository.ReminderRepository

class CreateReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) = repository.createReminder(reminder)
}
