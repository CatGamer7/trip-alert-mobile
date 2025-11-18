package com.example.tripalert.domain.usecase

import com.example.tripalert.domain.usecase.reminder.CreateReminderUseCase
import com.example.tripalert.domain.usecase.reminder.DeleteReminderUseCase
import com.example.tripalert.domain.usecase.reminder.GetRemindersForTripUseCase
import com.example.tripalert.domain.usecase.reminder.UpdateReminderUseCase

data class ReminderUseCases(
    val createReminder: CreateReminderUseCase,
    val updateReminder: UpdateReminderUseCase,
    val deleteReminder: DeleteReminderUseCase,
    val getRemindersForTrip: GetRemindersForTripUseCase
)
