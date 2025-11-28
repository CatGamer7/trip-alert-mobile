package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.repository.UserRepository

// ...
class CreateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(username: String, password: String, timeOffset: Int, preferredTransport: TransportType) =
        repository.createUser(username, password, timeOffset, preferredTransport)
}