package com.example.tripalert.ui.screens.triplist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripalert.R
import com.example.tripalert.domain.models.Trip // Используем модель Trip
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TripListScreen(
    navController: NavHostController,
    onAddClick: () -> Unit,
    onTripClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    // Получаем ViewModel через Koin
    viewModel: TripListViewModel = koinViewModel()
) {
    // Слушаем состояние ViewModel
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Эффект для отображения ошибки в Snackbar
    LaunchedEffect(state.error) {
        state.error?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
            viewModel.errorShown() // Сообщаем VM, что ошибка показана
        }
    }

    Scaffold(
        topBar = { DailyTripsTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.size(70.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Добавить",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // --- Список поездок ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // --- Ежедневные ---
                if (state.dailyTrips.isNotEmpty()) {
                    item {
                        Text(
                            text = "Ежедневно",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }

                    items(state.dailyTrips.size) { index ->
                        val trip = state.dailyTrips[index]
                        TripCard(
                            trip = trip, // Передаем реальный объект
                            onClick = { onTripClick(trip.id) }
                        )
                    }
                }

                // --- Другие ---
                if (state.otherTrips.isNotEmpty()) {
                    item {
                        Divider(
                            color = MaterialTheme.colorScheme.onBackground,
                            thickness = 2.dp,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        )
                    }

                    item {
                        Text(
                            text = "Другие",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }

                    items(state.otherTrips.size) { index ->
                        val trip = state.otherTrips[index]
                        TripCard(
                            trip = trip, // Передаем реальный объект
                            onClick = { onTripClick(trip.id) }
                        )
                    }
                }

                // --- Состояние пустого списка ---
                if (!state.isLoading && state.dailyTrips.isEmpty() && state.otherTrips.isEmpty()) {
                    item {
                        Text(
                            text = "Поездок пока нет. Нажмите + для добавления.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
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


@Composable
fun DailyTripsTopBar() {
    // ... (без изменений, остается прежним)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 18.dp)
    ) {
        // Тулбар
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть: аватарка + две кнопки
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            clip = false
                        )
                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                        .clip(CircleShape)
                        .clickable { /* действие */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Avatar",
                        modifier = Modifier.size(41.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Первая кнопка
                IconButton(onClick = { /* действие */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = "Icon 1",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Вторая кнопка
                IconButton(onClick = { /* действие */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.bell),
                        contentDescription = "Icon 2",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }


            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Планы",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.arrowright),
                        contentDescription = "Далее",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun TripCard(trip: Trip, onClick: () -> Unit) {
    // Вспомогательный форматтер для времени
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    // Вспомогательный форматтер для даты
    val dateFormatter = remember { DateTimeFormatter.ofPattern("d.MM.yyyy") }

    // ⚠️ ВРЕМЕННЫЕ ЗАГЛУШКИ ДЛЯ РАСЧЕТОВ
    // Это должно быть частью логики, но пока оставим как константы
    val travelTimeMinutes = 17
    val timeToLeave = trip.plannedTime.minusMinutes(travelTimeMinutes.toLong())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 1.dp)
            .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(35.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {

            // Заголовок
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        // Используем имя поездки (где хранятся адреса)
                        trip.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(4.dp))
                    // Отображаем иконку, соответствующую типу транспорта
                    Icon(
                        // В реальном проекте здесь должен быть маппинг TransportType -> Resource ID
                        painter = painterResource(id = R.drawable.runman),
                        contentDescription = trip.transportType.name,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(4.dp))
                // Здесь должна быть логика времени до уведомления (пока заглушка)
                Text(
                    "10 мин.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(Modifier.height(16.dp))

            // Время
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Время прибытия (plannedTime)
                TimeLabel("Время", trip.plannedTime.format(timeFormatter))

                Icon(
                    painter = painterResource(id = R.drawable.minus_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .offset(y = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                // Время в пути (заглушка)
                TimeLabel("Время в пути", "${travelTimeMinutes} мин.")

                Icon(
                    painter = painterResource(id = R.drawable.equal_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .offset(y = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                // Время выхода
                TimeLabel("Выход в", timeToLeave.format(timeFormatter))
            }

            Spacer(Modifier.height(16.dp))

            // Подвал
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Дата (Например: Среда, 22.10.2025)
                val dayOfWeek = trip.plannedTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru"))
                Text(
                    "${dayOfWeek.replaceFirstChar { it.uppercase() }}, ${trip.plannedTime.format(dateFormatter)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                // Адрес отправления
                Text(
                    "Из: ${trip.originAddress ?: "Не указано"}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Composable
private fun TimeLabel(label: String, value: String) {
    // ... (без изменений, остается прежним)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}