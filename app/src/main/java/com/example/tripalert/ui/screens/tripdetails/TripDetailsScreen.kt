package com.example.tripalert.ui.screens.tripdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
// ✅ ИСПРАВЛЕНО: Правильный импорт KeyboardOptions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripalert.R
import com.example.tripalert.domain.models.TransportType
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// ✅ ИСПРАВЛЕНО: Добавляем аннотацию OptIn для Material3 API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isCreationMode = state.trip.id == 0L

    // Переменная для управления состоянием активности FAB
    val isFabEnabled = !state.isSaving && !state.isLoading

    // --- Эффекты ---
    LaunchedEffect(state.saveSuccessful) {
        if (state.saveSuccessful) {
            onNavigateBack()
            viewModel.saveComplete()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { message ->
            snackbarHostState.showSnackbar(message = message, actionLabel = "OK", duration = SnackbarDuration.Short)
            viewModel.errorShown()
        }
    }

    // --- UI ---
    Scaffold(
        topBar = {
            TripDetailsTopBar(
                title = if (isCreationMode) "Создание поездки" else "Редактирование",
                onBackClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // ✅ ИСПРАВЛЕНО: Убираем параметр 'enabled' и используем условный onClick
            FloatingActionButton(
                // ✅ ИСПРАВЛЕННЫЙ ВАРИАНТ
                onClick = {
                    if (isFabEnabled) {
                        viewModel.saveTrip() // Вызываем метод
                    }
                },
                modifier = Modifier.size(70.dp),
                // Material 3 автоматически меняет цвет, если onClick == {}
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 3.dp, modifier = Modifier.size(24.dp))
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "Сохранить",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 1. КОНТЕЙНЕР ДЛЯ АДРЕСОВ
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(35.dp)),
                    shape = RoundedCornerShape(35.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Маршрут",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        AddressInputField(
                            value = state.trip.originAddress ?: "",
                            onValueChange = viewModel::updateOriginAddress,
                            label = "Откуда",
                            error = state.originAddressError,
                            leadingIcon = R.drawable.marker,
                        )

                        AddressInputField(
                            value = state.trip.destinationAddress ?: "",
                            onValueChange = viewModel::updateDestinationAddress,
                            label = "Куда",
                            error = state.destinationAddressError,
                            leadingIcon = R.drawable.marker,
                            imeAction = ImeAction.Done
                        )
                    }
                }

                // 2. ВРЕМЯ И ДАТА
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(35.dp)),
                    shape = RoundedCornerShape(35.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Время и дата",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        DateTimePicker(
                            dateTime = state.trip.plannedTime,
                            onDateTimeChange = viewModel::updatePlannedTime,
                            label = "Планируемое прибытие"
                        )

                        AlertTimePicker()
                    }
                }

                // 3. ВЫБОР ТРАНСПОРТА
                TransportSelector(
                    selectedType = state.trip.transportType,
                    onTypeSelected = {
                        // viewModel.updateTransportType(it)
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(64.dp))
            }

            // --- Индикатор загрузки ---
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// ✅ ИСПРАВЛЕНО: Добавляем аннотацию OptIn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title, color = MaterialTheme.colorScheme.onBackground) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowdown),
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun AddressInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    leadingIcon: Int,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            isError = error != null,
            // ✅ ИСПРАВЛЕНО: Используем правильный KeyboardOptions
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = label,
                    tint = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            shape = RoundedCornerShape(12.dp)
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun DateTimePicker(
    dateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit,
    label: String
) {
    val formatter = remember { DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale("ru")) }

    OutlinedTextField(
        value = dateTime.format(formatter),
        onValueChange = { /* Только для чтения */ },
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "Выбрать дату/время",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Вызов Dialogs
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(12.dp)
    )
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
            .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(35.dp)),
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Транспорт",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TransportType.entries.forEach { type ->
                    val isSelected = type == selectedType
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTypeSelected(type) }
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                            .border(2.dp, if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.name.replaceFirstChar { it.uppercase() },
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlertTimePicker(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Уведомление:", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { /* Вызов диалога выбора времени */ }
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Время уведомления",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("За 10 мин. до выхода", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}