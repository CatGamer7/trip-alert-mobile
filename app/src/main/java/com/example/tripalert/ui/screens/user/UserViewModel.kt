package com.example.tripalert.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.data.remote.dto.UpdateUserDTO
import com.example.tripalert.domain.models.TransportType
import kotlinx.coroutines.launch

class UserViewModel(
    private val api: UserApi
) : ViewModel() {

    fun saveUser(
        userId: Long,
        username: String,
        password: String,
        timeOffset: Int,
        preferredTransport: TransportType,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (userId == 0L) {
                    // Создаём нового пользователя
                    val dto = CreateUserDTO(username, password, timeOffset, preferredTransport)
                    val response = api.createUser(dto)
                    onResult("Created: ${response.username}")
                } else {
                    // Обновляем существующего пользователя
                    val dto = UpdateUserDTO(timeOffset, preferredTransport)
                    val response = api.updateUser(userId, dto)
                    onResult("Updated: ${response.username}")
                }
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }

    fun getUserProfile(
        userId: Long,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.getUserById(userId)
                onResult(
                    "Username: ${response.username}\n" +
                            "TimeOffset: ${response.timeOffset}\n" +
                            "Transport: ${response.preferredTransport}"
                )
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }
}
