package com.example.tripalert.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tripalert.domain.models.TransportType
import org.koin.androidx.compose.getViewModel

@Composable
fun UserScreen(
    navController: NavController,
    userId: Long // динамический userId
) {
    val viewModel: UserViewModel = getViewModel()
    var username by remember { mutableStateOf("Mih Butovskiy") }
    var password by remember { mutableStateOf("тут будет пароль") }
    var offset by remember { mutableStateOf("10") }
    var transport by remember { mutableStateOf(TransportType.WALK) }
    var serverResponse by remember { mutableStateOf("") }

    // Авто-загрузка данных при userId != 0
    LaunchedEffect(userId) {
        if (userId != 0L) {
            viewModel.getUserProfile(userId) { response ->
                serverResponse = response
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("User Profile", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = offset,
            onValueChange = { offset = it },
            label = { Text("Time Offset") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row {
            TransportType.values().forEach { t ->
                Button(
                    onClick = { transport = t },
                    colors = if (transport == t) ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    else ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(t.name)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveUser(
                    userId = userId,
                    username = username,
                    password = password,
                    timeOffset = offset.toIntOrNull() ?: 10,
                    preferredTransport = transport
                ) { response ->
                    serverResponse = response
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (userId == 0L) "Create Profile" else "Update Profile")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.getUserProfile(userId) { response ->
                    serverResponse = response
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Profile")
        }

        Spacer(Modifier.height(16.dp))

        Text("Server Response:\n$serverResponse")
    }
}
