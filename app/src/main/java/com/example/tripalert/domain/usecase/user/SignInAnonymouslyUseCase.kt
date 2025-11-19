package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.repository.UserRepository

class SignInAnonymouslyUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.signInAnonymously()
}
