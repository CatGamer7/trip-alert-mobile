package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.repository.UserRepository

class DeleteUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.deleteUser()
}