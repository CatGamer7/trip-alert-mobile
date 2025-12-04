package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(username: String, pass: String)
    suspend fun signOut()

    fun getCurrentAuthToken(): String?
    fun getCurrentUserName(): String?

    suspend fun createUser(user: User, pass: String)
    suspend fun updateProfile(user: User)
    suspend fun deleteUser()

    suspend fun fetchProfileFromServer()
    fun getUserProfileFlow(): Flow<User?>
}