package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository

class CreateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(username: String, pass: String, offset: Int, transport: TransportType) {
        val newUser = User(username, offset, transport)
        repository.createUser(newUser, pass)
    }
}