//package com.example.tripalert.ui.screens.auth
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import org.koin.androidx.compose.koinViewModel
//
//@Composable
//fun AuthScreen(navController: NavController, viewModel: UserAuthViewModel = koinViewModel()) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var userId by remember { mutableStateOf("") }
//    val authState by viewModel.authState.collectAsState()
//
//    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Top) {
//
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        Button(onClick = { viewModel.signUp(username, password) }, modifier = Modifier.fillMaxWidth()) {
//            Text("Sign Up")
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = userId,
//            onValueChange = { userId = it },
//            label = { Text("User ID") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(onClick = { viewModel.signIn(userId.toLongOrNull() ?: 0L) }, modifier = Modifier.fillMaxWidth()) {
//            Text("Sign In")
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        Text("State: $authState")
//    }
//}
