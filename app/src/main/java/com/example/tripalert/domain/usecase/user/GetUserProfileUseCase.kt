package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val repository: UserRepository) {
    // Больше не нужен username, так как репозиторий знает текущего пользователя
    // Flow теперь выдает User? (может быть null, если не залогинен)
    operator fun invoke(): Flow<User?> = repository.getUserProfileFlow()
}