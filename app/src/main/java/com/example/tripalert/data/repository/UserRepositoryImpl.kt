package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.data.remote.dto.CreateUserDTO
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    private val _userFlow = MutableStateFlow<User?>(null)

    override fun getUserProfile(userId: Long): Flow<User> =
        _userFlow.filterNotNull().filter { it.id == userId }

    override fun getCurrentUserId(): String =
        _userFlow.value?.id?.toString() ?: ""

    override suspend fun updateProfile(user: User) {
        val dto = api.updateUser(user.id, UserMapper.toUpdateDto(user))
        _userFlow.value = UserMapper.fromDto(dto)
    }

    override suspend fun signOut() {
        _userFlow.value = null
    }

    override suspend fun signInAnonymously() {
        val created = api.createUser(
            CreateUserDTO(
                username = "Anonymous",
                password = "123456",
                timeOffset = 10,
                preferredTransport = TransportType.WALK
            )
        )
        _userFlow.value = UserMapper.fromDto(created)
    }
}
