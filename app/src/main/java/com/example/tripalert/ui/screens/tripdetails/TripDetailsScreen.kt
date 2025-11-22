package com.example.tripalert.ui.screens.tripdetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image // ✅ Добавлен явный импорт Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripalert.R
import com.example.tripalert.domain.models.GeoPoint
import com.example.tripalert.domain.models.TransportType
import com.example.tripalert.ui.theme.GreenPrimary
import com.example.tripalert.ui.theme.GreyBackground
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    onNavigateBack: () -> Unit,
    tripId: Long,
    viewModel: TripDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }

    val isCreationMode = state.trip.id == 0L
    val dateFormatter = remember { DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru")) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    // --- Дефолтные координаты ---
    val defaultOrigin = GeoPoint(55.7558, 37.6173) // Москва
    val defaultDestination = GeoPoint(59.9343, 30.3351) // Санкт-Петербург

    // --- Локальное состояние для полей координат ---
    var origin by remember { mutableStateOf(state.trip.origin ?: defaultOrigin) }
    var destination by remember { mutableStateOf(state.trip.destination ?: defaultDestination) }
    var originText by remember { mutableStateOf("${origin.latitude}, ${origin.longitude}") }
    var destinationText by remember { mutableStateOf("${destination.latitude}, ${destination.longitude}") }

    // При создании новой поездки сразу обновляем ViewModel
    LaunchedEffect(state.trip.id) {
        if (isCreationMode) {
            viewModel.updateOriginCoordinates(origin)
            viewModel.updateDestinationCoordinates(destination)
        }
    }

    LaunchedEffect(state.saveSuccessful) {
        if (state.saveSuccessful) {
            onNavigateBack()
            viewModel.saveComplete()
        }
    }

    fun showDatePicker() {
        val current = state.trip.plannedTime
        DatePickerDialog(context, { _, y, m, d ->
            viewModel.updatePlannedTime(current.withYear(y).withMonth(m + 1).withDayOfMonth(d))
        }, current.year, current.monthValue - 1, current.dayOfMonth).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    fun showTimePicker() {
        val current = state.trip.plannedTime
        TimePickerDialog(context, { _, h, m ->
            viewModel.updatePlannedTime(current.withHour(h).withMinute(m))
        }, current.hour, current.minute, true).show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = { SimpleTopBar(onBackClick = onNavigateBack) }, containerColor = Color.White) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (isCreationMode) "Новая поездка" else "Редактировать поездку",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }

                // --- Поля с координатами ---
                item {
                    InputCard(
                        label = "Откуда",
                        value = originText,
                        onValueChange = {
                            originText = it
                            val parts = it.split(",").mapNotNull { s -> s.trim().toDoubleOrNull() }
                            if (parts.size == 2) {
                                origin = GeoPoint(parts[0], parts[1])
                                viewModel.updateOriginCoordinates(origin)
                            }
                        },
                        painterResource(id = R.drawable.marker)
                    )
                }
                item {
                    InputCard(
                        label = "Куда",
                        value = destinationText,
                        onValueChange = {
                            destinationText = it
                            val parts = it.split(",").mapNotNull { s -> s.trim().toDoubleOrNull() }
                            if (parts.size == 2) {
                                destination = GeoPoint(parts[0], parts[1])
                                viewModel.updateDestinationCoordinates(destination)
                            }
                        },
                        painterResource(id = R.drawable.marker)
                    )
                }

                item { InputCard("Когда", state.trip.plannedTime.format(dateFormatter), {}, painterResource(id = R.drawable.calendar), onIconClick = { showDatePicker() }, readOnly = true) }
                item { InputCard("Прибытие", state.trip.plannedTime.format(timeFormatter), {}, painterResource(id = R.drawable.clock), onIconClick = { showTimePicker() }, readOnly = true) }

                val travelTimeMinutes = 15 // заглушка
                item {
                    Text(
                        "Выход в: ${state.trip.plannedTime.minusMinutes(travelTimeMinutes.toLong()).format(timeFormatter)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                item { DropdownInputCard("Напомнить за", "10 минут", listOf("5 минут","10 минут","15 минут","30 минут","1 час")) { /* TODO */ } }
                item { TransportSelector(selectedType = state.trip.transportType, onTypeSelected = viewModel::updateTransportType, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) }
                item { Spacer(modifier = Modifier.height(120.dp)) }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isCreationMode) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.Red.copy(alpha = 0.8f))
                        .clickable { showConfirmDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(id = R.drawable.trash), contentDescription = "Удалить", tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            val buttonText = if (isCreationMode) "Добавить поездку" else "Сохранить"
            val buttonWidth = if (isCreationMode) 290.dp else 196.dp

            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .height(60.dp)
                    .shadow(10.dp, RoundedCornerShape(28.dp))
                    .background(GreenPrimary, RoundedCornerShape(28.dp))
                    .clickable { viewModel.saveTrip() },
                contentAlignment = Alignment.Center
            ) {
                if (state.isSaving) CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                else Text(buttonText, color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (showConfirmDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .clickable(enabled = true) { showConfirmDialog = false },
                contentAlignment = Alignment.Center
            ) {
                ConfirmDeleteDialog(
                    onConfirm = { /* TODO: viewModel.deleteTrip() */ showConfirmDialog = false; onNavigateBack() },
                    onDismiss = { showConfirmDialog = false }
                )
            }
        }
    }
}




// --- Компонент ввода координат ---
@Composable
fun TripCoordinatesInput(
    origin: GeoPoint,
    destination: GeoPoint,
    onOriginChange: (GeoPoint) -> Unit,
    onDestinationChange: (GeoPoint) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text("Откуда", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, fontSize = 20.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = origin.latitude.toString(),
                onValueChange = { newLat ->
                    val lat = newLat.toDoubleOrNull() ?: origin.latitude
                    onOriginChange(GeoPoint(lat, origin.longitude))
                },
                label = { Text("Широта") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = origin.longitude.toString(),
                onValueChange = { newLon ->
                    val lon = newLon.toDoubleOrNull() ?: origin.longitude
                    onOriginChange(GeoPoint(origin.latitude, lon))
                },
                label = { Text("Долгота") },
                modifier = Modifier.weight(1f)
            )
        }

        Text("Куда", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, fontSize = 20.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = destination.latitude.toString(),
                onValueChange = { newLat ->
                    val lat = newLat.toDoubleOrNull() ?: destination.latitude
                    onDestinationChange(GeoPoint(lat, destination.longitude))
                },
                label = { Text("Широта") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = destination.longitude.toString(),
                onValueChange = { newLon ->
                    val lon = newLon.toDoubleOrNull() ?: destination.longitude
                    onDestinationChange(GeoPoint(destination.latitude, lon))
                },
                label = { Text("Долгота") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// --- ВСПОМОГАТЕЛЬНЫЕ КОМПОНЕНТЫ ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("", color = MaterialTheme.colorScheme.onBackground) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowdown),
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: Painter? = null,
    onIconClick: (() -> Unit)? = null,
    readOnly: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 20.sp)

        OutlinedTextField(
            value = value,
            onValueChange = if (readOnly) { {} } else onValueChange,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x33000000)
                )
                .background(GreyBackground, RoundedCornerShape(28.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GreyBackground,
                unfocusedContainerColor = GreyBackground,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            singleLine = true,
            trailingIcon = {
                if (icon != null) {
                    Image(
                        icon, null, Modifier
                            .size(24.dp)
                            .clickable(enabled = onIconClick != null) { onIconClick?.invoke() }
                            .padding(end = 4.dp)
                    )
                }
            }
        )
    }
}

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
                .height(60.dp)
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
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
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

@Composable
fun TransportSelector(
    selectedType: TransportType,
    onTypeSelected: (TransportType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(35.dp))
            .shadow(8.dp, RoundedCornerShape(35.dp), ambientColor = Color(0x33000000)),
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = GreyBackground)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Транспорт", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                // Безопасное использование values() для перечисления
                TransportType.values().forEach { type ->
                    val isSelected = type == selectedType
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTypeSelected(type) }
                            .background(if (isSelected) GreenPrimary else Color.White)
                            .border(2.dp, if (isSelected) Color.Black else Color.LightGray, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.name.lowercase().replaceFirstChar { it.titlecase(Locale.ROOT) },
                            color = if (isSelected) Color.Black else Color.DarkGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

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
            .clip(RoundedCornerShape(20.dp))
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