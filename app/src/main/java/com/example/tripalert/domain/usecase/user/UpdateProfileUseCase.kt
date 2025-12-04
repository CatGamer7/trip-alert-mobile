package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository

class UpdateProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        repository.updateProfile(user)
    }
}