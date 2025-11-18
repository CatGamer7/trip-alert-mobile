package com.example.tripalert.domain.usecase.reminder

import com.example.tripalert.domain.repository.ReminderRepository

class DeleteReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminderId: Int) =
        repository.deleteReminder(reminderId.toLong())

}
