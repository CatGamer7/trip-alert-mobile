//package com.example.tripalert.ui.screens.auth
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.tripalert.domain.repository.AuthRepository
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//class UserAuthViewModel(private val repository: AuthRepository) : ViewModel() {
//
//    private val _authState = MutableStateFlow("Idle")
//    val authState: StateFlow<String> = _authState
//
//    fun signUp(username: String, password: String) {
//        viewModelScope.launch {
//            try {
//                val user = repository.signUp(username, password)
//                _authState.value = "Created: ${user.username} (id=${user.username})"
//            } catch (e: Exception) {
//                _authState.value = "Error: ${e.message}"
//            }
//        }
//    }
//
//    fun signIn(userId: Long) {
//        viewModelScope.launch {
//            try {
//                val user = repository.signIn(userId)
//                _authState.value = "Signed in: ${user.username} (id=${userId})"
//            } catch (e: Exception) {
//                _authState.value = "Error: ${e.message}"
//            }
//        }
//    }
//}
