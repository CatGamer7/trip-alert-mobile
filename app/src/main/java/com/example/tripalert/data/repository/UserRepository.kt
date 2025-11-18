package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(userId: Long): Flow<User>
    suspend fun updateProfile(user: User)
    fun getCurrentUserId(): String
    suspend fun signOut()
    suspend fun signInAnonymously()
}
