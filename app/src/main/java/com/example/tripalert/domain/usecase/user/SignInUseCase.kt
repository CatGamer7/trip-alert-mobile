package com.example.tripalert.domain.usecase.user

import com.example.tripalert.domain.repository.UserRepository

class SignInUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(username: String, password: String) = repository.signIn(username, password)
}