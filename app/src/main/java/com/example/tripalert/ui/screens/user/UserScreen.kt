package com.example.tripalert.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tripalert.domain.models.TransportType
import org.koin.androidx.compose.getViewModel

@Composable
fun UserScreen(navController: NavController) {
    val viewModel: UserViewModel = getViewModel()

    var username by remember { mutableStateOf("Mih Butovskiy") }
    var password by remember { mutableStateOf("password") }
    var offset by remember { mutableStateOf("10") }
    var transport by remember { mutableStateOf(TransportType.WALK) }

    val serverResponse by viewModel.serverResponse.collectAsState()
    val currentUser by viewModel.currentUserState.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            username = it.username
            offset = it.timeOffset.toString()
            transport = it.preferredTransport
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text("TripAlert Profile", style = MaterialTheme.typography.headlineMedium)

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
            visualTransformation = PasswordVisualTransformation(),
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
        Text("Preferred Transport:", fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TransportType.values().forEach { t ->
                val isSelected = transport == t
                Button(
                    onClick = { transport = t },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.weight(1f).padding(2.dp)
                ) {
                    Text(t.name, fontSize = 10.sp)
                }
            }
        }

        Divider(Modifier.padding(vertical = 16.dp))

        Text("Auth Actions", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    viewModel.createUser(
                        username, password, offset.toIntOrNull() ?: 0, transport
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Register")
            }

            Button(
                onClick = {
                    viewModel.signIn(username, password)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Login")
            }
        }

        Spacer(Modifier.height(8.dp))

        Text("User Actions (Need Login)", style = MaterialTheme.typography.titleMedium)

        Button(
            onClick = { viewModel.fetchProfile() },
            modifier = Modifier.fillMaxWidth(),
            enabled = currentUser != null
        ) {
            Text("Fetch Profile")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.updateUser(offset.toIntOrNull(), transport)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Profile")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { viewModel.deleteUser() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("DELETE USER")
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().background(Color.LightGray),
        ) {
            Text(
                text = "Log: $serverResponse",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}