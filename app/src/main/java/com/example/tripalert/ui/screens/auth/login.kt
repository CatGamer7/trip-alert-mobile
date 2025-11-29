//package com.example.login
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun LoginScreen() {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var rememberMe by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFE8F0E8))
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp)
//        ) {
//            // Шапка с кнопкой назад
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp, bottom = 32.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(
//                    onClick = { /* Назад */ },
//                    modifier = Modifier
//                        .size(48.dp)
//                        .background(Color(0xFFC8DCC8), CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Назад",
//                        tint = Color(0xFF2D4A2D)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                Text(
//                    text = "Вход",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color(0xFF1C1C1C)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Имя пользователя
//            Text(
//                text = "Имя пользователя",
//                fontSize = 13.sp,
//                color = Color(0xFF1C1C1C),
//                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
//            )
//
//            OutlinedTextField(
//                value = username,
//                onValueChange = { username = it },
//                placeholder = { Text("Username", color = Color(0xFF8A8A8A)) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(28.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color(0xFFD5E3D5),
//                    unfocusedContainerColor = Color(0xFFD5E3D5),
//                    disabledContainerColor = Color(0xFFD5E3D5),
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent,
//                ),
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Пароль
//            Text(
//                text = "Пароль",
//                fontSize = 13.sp,
//                color = Color(0xFF1C1C1C),
//                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
//            )
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                placeholder = { Text("Password", color = Color(0xFF8A8A8A)) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(28.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color(0xFFD5E3D5),
//                    unfocusedContainerColor = Color(0xFFD5E3D5),
//                    disabledContainerColor = Color(0xFFD5E3D5),
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent,
//                ),
//                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                trailingIcon = {
//                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                        Icon(
//                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
//                            tint = Color(0xFF6B6B6B)
//                        )
//                    }
//                },
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Запомнить меня и Забыли пароль?
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Checkbox(
//                        checked = rememberMe,
//                        onCheckedChange = { rememberMe = it },
//                        colors = CheckboxDefaults.colors(
//                            checkedColor = Color(0xFF4A7C59),
//                            uncheckedColor = Color(0xFF6B6B6B),
//                            checkmarkColor = Color.White
//                        )
//                    )
//                    Text(
//                        text = "Запомнить меня",
//                        fontSize = 13.sp,
//                        color = Color(0xFF1C1C1C)
//                    )
//                }
//
//                TextButton(onClick = { /* Восстановление пароля */ }) {
//                    Text(
//                        text = "Забыли пароль?",
//                        fontSize = 13.sp,
//                        color = Color(0xFF4A7C59),
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Кнопка входа
//            Button(
//                onClick = { /* Вход */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(28.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFA8C8A8)
//                )
//            ) {
//                Text(
//                    text = "Войти",
//                    fontSize = 17.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color(0xFF1C1C1C)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Нет аккаунта?
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 24.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "Нет аккаунта? ",
//                    fontSize = 14.sp,
//                    color = Color(0xFF1C1C1C)
//                )
//                Text(
//                    text = "Зарегистрироваться",
//                    fontSize = 14.sp,
//                    color = Color(0xFF4A7C59),
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//}