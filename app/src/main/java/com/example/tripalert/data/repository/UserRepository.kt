package com.example.tripalert.domain.repository

import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfileFlow(): Flow<User?>
    fun getCurrentAuthToken(): String?

    // Операции
    suspend fun signIn(username: String, password: String)
    suspend fun createUser(username: String, password: String, timeOffset: Int, preferredTransport: TransportType)
    suspend fun fetchProfileFromServer()
    suspend fun updateProfile(timeOffset: Int?, preferredTransport: TransportType?)
    suspend fun deleteUser()
    suspend fun signOut()

    fun getCurrentUserName(): String?
}