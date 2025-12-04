package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<User?> = repository.getUserProfileFlow()
}