package com.example.tripalert.domain.usecase

import com.example.tripalert.domain.usecase.user.GetUserProfileUseCase
import com.example.tripalert.domain.usecase.user.SignInUseCase
import com.example.tripalert.domain.usecase.user.SignOutUseCase
import com.example.tripalert.domain.usecase.user.UpdateProfileUseCase

data class UserUseCases(
    val getUserProfile: GetUserProfileUseCase,
    val updateProfile: UpdateProfileUseCase,
    val signOut: SignOutUseCase,
    val signInAnonymously: SignInUseCase
)
