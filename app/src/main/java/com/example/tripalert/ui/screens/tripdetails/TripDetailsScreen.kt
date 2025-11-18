package com.example.tripalert.ui.screens.tripdetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripalert.R
import com.example.tripalert.ui.screens.triplist.DailyTripsTopBar
import com.example.tripalert.ui.theme.GreenPrimary
import com.example.tripalert.ui.theme.GreyBackground
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TripDetailsScreen(
    tripId: Long? = null,
    isEditing: Boolean = false,
    viewModel: TripDetailsViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var showConfirmDialog by remember { mutableStateOf(false) }

    // --- Инициализация ---
    LaunchedEffect(tripId, isEditing) {
        viewModel.setEditing(isEditing)
        tripId?.let { viewModel.loadTrip(it) }

        if (state.date.isBlank()) {
            val today = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
            viewModel.updateField(date = today)
        }

        if (state.departureTime.isBlank() && state.arrivalTime.isNotBlank()) {
            viewModel.calculateDeparture(state.arrivalTime, 17)
        }
    }

    // --- Диалоги выбора даты и времени ---
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selected = String.format("%02d.%02d.%04d", day, month + 1, year)
                viewModel.updateField(date = selected)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = calendar.timeInMillis }
    }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val arrival = String.format("%02d:%02d", hour, minute)
                viewModel.updateField(arrival = arrival)
                viewModel.calculateDeparture(arrival, 17)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { DailyTripsTopBar() },
            containerColor = Color.White
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (state.isEditing) "Редактировать поездку" else "Новая поездка",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item { InputCard("Откуда", state.fromAddress, { viewModel.updateField(from = it) }, icon = painterResource(id = R.drawable.marker)) }
                item { InputCard("Куда", state.toAddress, { viewModel.updateField(to = it) }, icon = painterResource(id = R.drawable.marker)) }
                item { InputCard("Когда", state.date, {}, icon = painterResource(id = R.drawable.calendar), onIconClick = { datePickerDialog.show() }) }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InputCard("Прибытие", state.arrivalTime, {}, icon = painterResource(id = R.drawable.clock), onIconClick = { timePickerDialog.show() })
                        Text(
                            text = "Выход в: ${state.departureTime}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
                item { DropdownInputCard("Напомнить за", state.reminderTime, listOf("5 минут","10 минут","15 минут","30 минут","1 час"), onOptionSelected = { viewModel.updateField(reminderTime = it) }) }
                item { Spacer(modifier = Modifier.height(120.dp)) }
            }
        }

        // --- Кнопки добавления / сохранения / удаления ---
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (state.isEditing) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(GreenPrimary)
                        .clickable { showConfirmDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(id = R.drawable.trash), contentDescription = "Удалить", tint = Color.Black, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .width(196.dp)
                        .height(60.dp)
                        .shadow(8.dp, RoundedCornerShape(28.dp))
                        .background(GreenPrimary, RoundedCornerShape(28.dp))
                        .clickable { viewModel.saveTrip {} },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Сохранить", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Box(
                    modifier = Modifier
                        .width(290.dp)
                        .height(60.dp)
                        .shadow(10.dp, RoundedCornerShape(28.dp))
                        .background(GreenPrimary, RoundedCornerShape(28.dp))
                        .clickable { viewModel.saveTrip {} },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Добавить поездку", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- Диалог удаления ---
        if (showConfirmDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .clickable(enabled = true) {},
                contentAlignment = Alignment.Center
            ) {
                ConfirmDeleteDialog(
                    onConfirm = {
                        viewModel.deleteTrip {}
                        showConfirmDialog = false
                    },
                    onDismiss = { showConfirmDialog = false }
                )
            }
        }
    }
}

// --- Composable для ввода ---
@Composable
fun InputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: Painter? = null,
    onIconClick: (() -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 20.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x33000000)
                )
                .background(GreyBackground, RoundedCornerShape(28.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(28.dp))
                .clickable(enabled = onIconClick != null) { onIconClick?.invoke() }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = value, color = Color.Black, fontSize = 20.sp)
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(enabled = onIconClick != null) { onIconClick?.invoke() }
                    )
                }
            }
        }
    }
}

// --- Выпадающий список ---
@Composable
fun DropdownInputCard(
    label: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 20.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x33000000)
                )
                .background(GreyBackground, RoundedCornerShape(28.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(28.dp))
                .clickable { expanded = !expanded }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = value, fontWeight = FontWeight.Medium, fontSize = 20.sp, color = Color.Black)

                Icon(
                    painter = painterResource(id = R.drawable.arrowdown),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { expanded = !expanded }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(GreyBackground),
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// --- Диалог подтверждения удаления ---
@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(20.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Вы уверены?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Это действие нельзя отменить.",
                fontSize = 18.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .shadow(8.dp, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text("Да", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .shadow(8.dp, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Отмена", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}
