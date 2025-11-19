package com.example.tripalert.data.remote.api

import com.example.tripalert.data.remote.dto.CreateReminderDTO
import com.example.tripalert.data.remote.dto.ReminderResponseDTO
import com.example.tripalert.data.remote.dto.UpdateReminderDTO
import retrofit2.http.*

interface ReminderApi {

    @POST("reminders")
    suspend fun createReminder(@Body reminder: CreateReminderDTO): ReminderResponseDTO

    @GET("reminders")
    suspend fun getReminders(): List<ReminderResponseDTO>

    @GET("reminders/{id}")
    suspend fun getReminderById(@Path("id") id: Long): ReminderResponseDTO

    @PUT("reminders/{id}")
    suspend fun updateReminder(@Path("id") id: Long, @Body updates: UpdateReminderDTO): ReminderResponseDTO

    @DELETE("reminders/{id}")
    suspend fun deleteReminder(@Path("id") id: Long)
}
