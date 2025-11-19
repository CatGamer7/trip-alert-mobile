package com.example.tripalert.domain.usecase.reminder

import com.example.tripalert.domain.models.Reminder
import com.example.tripalert.domain.repository.ReminderRepository

class UpdateReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) = repository.updateReminder(reminder)
}
