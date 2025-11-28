//package com.example.tripalert.domain.repository
//
//import com.example.tripalert.data.remote.api.UserApi
//import com.example.tripalert.data.remote.dto.CreateUserDTO
//import com.example.tripalert.data.remote.dto.UpdateUserDTO
//import com.example.tripalert.domain.models.TransportType
//
//class AuthRepository(private val api: UserApi) {
//
//    suspend fun signUp(
//        username: String,
//        password: String,
//        timeOffset: Int = 10,
//        preferredTransport: TransportType = TransportType.WALK
//    ) = api.createUser(
//        CreateUserDTO(username, password, timeOffset, preferredTransport)
//    )
//
//    suspend fun signIn(userId: Long) = api.getUserById(userId)
//
// suspend fun updateProfile(
//        userId: Long,
//        username: String? = null,
//        password: String? = null,
//        timeOffset: Int? = null,
//        preferredTransport: TransportType? = null
//    ) = api.updateUser(
//        userId,
//        UpdateUserDTO(timeOffset, preferredTransport,)
//    )
//}
