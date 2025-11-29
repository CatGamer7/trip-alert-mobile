//package com.example.profile
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.CameraAlt
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun ProfileScreen() {
//    var username by remember { mutableStateOf("John_Doe") }
//    var currentPassword by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var currentPasswordVisible by remember { mutableStateOf(false) }
//    var newPasswordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//    var isEditingUsername by remember { mutableStateOf(false) }
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
//                    .padding(top = 16.dp, bottom = 24.dp),
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
//                    text = "Профиль",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color(0xFF1C1C1C)
//                )
//            }
//
//            // Аватар
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(vertical = 20.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(120.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFD5E3D5))
//                        .border(3.dp, Color(0xFFA8C8A8), CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "Аватар",
//                        modifier = Modifier.size(60.dp),
//                        tint = Color(0xFF6B6B6B)
//                    )
//                }
//
//                // Кнопка изменения фото
//                IconButton(
//                    onClick = { /* Изменить фото */ },
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .size(40.dp)
//                        .background(Color(0xFFA8C8A8), CircleShape)
//                        .border(2.dp, Color(0xFFE8F0E8), CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CameraAlt,
//                        contentDescription = "Изменить фото",
//                        tint = Color(0xFF2D4A2D),
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
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
//                trailingIcon = {
//                    IconButton(onClick = { isEditingUsername = !isEditingUsername }) {
//                        Icon(
//                            imageVector = Icons.Default.Edit,
//                            contentDescription = "Редактировать",
//                            tint = Color(0xFF6B6B6B)
//                        )
//                    }
//                },
//                enabled = isEditingUsername,
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Изменить пароль
//            Text(
//                text = "Изменить пароль",
//                fontSize = 15.sp,
//                fontWeight = FontWeight.Medium,
//                color = Color(0xFF1C1C1C),
//                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
//            )
//
//            // Текущий пароль
//            Text(
//                text = "Текущий пароль",
//                fontSize = 13.sp,
//                color = Color(0xFF1C1C1C),
//                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
//            )
//
//            OutlinedTextField(
//                value = currentPassword,
//                onValueChange = { currentPassword = it },
//                placeholder = { Text("Введите текущий пароль", color = Color(0xFF8A8A8A)) },
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
//                visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                trailingIcon = {
//                    IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
//                        Icon(
//                            imageVector = if (currentPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = if (currentPasswordVisible) "Скрыть пароль" else "Показать пароль",
//                            tint = Color(0xFF6B6B6B)
//                        )
//                    }
//                },
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Новый пароль
//            Text(
//                text = "Новый пароль",
//                fontSize = 13.sp,
//                color = Color(0xFF1C1C1C),
//                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
//            )
//
//            OutlinedTextField(
//                value = newPassword,
//                onValueChange = { newPassword = it },
//                placeholder = { Text("Введите новый пароль", color = Color(0xFF8A8A8A)) },
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
//                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                trailingIcon = {
//                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
//                        Icon(
//                            imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = if (newPasswordVisible) "Скрыть пароль" else "Показать пароль",
//                            tint = Color(0xFF6B6B6B)
//                        )
//                    }
//                },
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Подтвердите пароль
//            Text(
//                text = "Подтвердите пароль",
//                fontSize = 13.sp,
//                color = Color(0xFF1C1C1C),
//                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
//            )
//
//            OutlinedTextField(
//                value = confirmPassword,
//                onValueChange = { confirmPassword = it },
//                placeholder = { Text("Повторите новый пароль", color = Color(0xFF8A8A8A)) },
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
//                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                trailingIcon = {
//                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
//                        Icon(
//                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = if (confirmPasswordVisible) "Скрыть пароль" else "Показать пароль",
//                            tint = Color(0xFF6B6B6B)
//                        )
//                    }
//                },
//                singleLine = true
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Кнопка сохранения
//            Button(
//                onClick = { /* Сохранить изменения */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(28.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFA8C8A8)
//                )
//            ) {
//                Text(
//                    text = "Сохранить изменения",
//                    fontSize = 17.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color(0xFF1C1C1C)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}