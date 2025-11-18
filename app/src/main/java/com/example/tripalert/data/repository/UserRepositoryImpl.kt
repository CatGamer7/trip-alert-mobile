package com.example.tripalert.data.repository

import com.example.tripalert.data.mapper.UserMapper
import com.example.tripalert.data.remote.api.UserApi
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.domain.models.User
import com.example.tripalert.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

class UserRepositoryImpl(private val api: UserApi) : UserRepository {

    private val _userFlow = MutableStateFlow<User?>(null)
    override fun getUserProfile(userId: Long): Flow<User> {
        return _userFlow.filterNotNull().filter { it.id == userId }
    }
    override fun getCurrentUserId(): String = _userFlow.value?.id.toString()

    override suspend fun updateProfile(user: User) {
        val updated = api.updateUser(user.id, UserMapper.toUpdateDto(user))
        _userFlow.value = UserMapper.fromDto(updated)
    }

    override suspend fun signOut() { _userFlow.value = null }

    override suspend fun signInAnonymously() {
        val anon = api.createUser(UserMapper.toCreateDto(User(
            id = 0, username = "Anonymous", email = "", password = "",
            timeOffset = 15, preferredTransport = TransportType.WALK
        )))
        _userFlow.value = UserMapper.fromDto(anon)
    }
}