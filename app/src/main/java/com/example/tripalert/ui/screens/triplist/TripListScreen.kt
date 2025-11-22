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
import com.example.tripalert.domain.models.Trip
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TripListScreen(
    navController: NavHostController,
    onAddClick: () -> Unit,
    onTripClick: (Long) -> Unit,
    onUserClick: () -> Unit, // ← добавлен колбэк для аватара пользователя
    modifier: Modifier = Modifier,
    viewModel: TripListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
            viewModel.errorShown()
        }
    }

    Scaffold(
        topBar = { DailyTripsTopBar(navController = navController, onUserClick = onUserClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.size(70.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(painter = painterResource(id = R.drawable.plus), contentDescription = "Добавить")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {

                if (state.dailyTrips.isNotEmpty()) {
                    item { Text("Ежедневно", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
                    items(state.dailyTrips.size) { index ->
                        val trip = state.dailyTrips[index]
                        TripCard(trip = trip, onClick = { onTripClick(trip.id) })
                    }
                }

                if (state.otherTrips.isNotEmpty()) {
                    item { Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()) }
                    item { Text("Другие", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
                    items(state.otherTrips.size) { index ->
                        val trip = state.otherTrips[index]
                        TripCard(trip = trip, onClick = { onTripClick(trip.id) })
                    }
                }

                if (!state.isLoading && state.dailyTrips.isEmpty() && state.otherTrips.isEmpty()) {
                    item { Text("Поездок пока нет. Нажмите + для добавления.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)) }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun DailyTripsTopBar(
    navController: NavHostController,
    onUserClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

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
                        .clickable { onUserClick() }, // ← используем колбэк
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Avatar",
                        modifier = Modifier.size(41.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
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
                    .clickable {
                        navController.navigate(TripAlertDestinations.TRIP_LIST_ROUTE) {
                            popUpTo(TripAlertDestinations.TRIP_LIST_ROUTE) { inclusive = true }
                        }
                    }
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
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("d.MM.yyyy") }
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        trip.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
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
                Text(
                    "10 мин.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeLabel("Время", trip.plannedTime.format(timeFormatter))
                Icon(
                    painter = painterResource(id = R.drawable.minus_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp).offset(y = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                TimeLabel("Время в пути", "${travelTimeMinutes} мин.")
                Icon(
                    painter = painterResource(id = R.drawable.equal_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp).offset(y = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                TimeLabel("Выход в", timeToLeave.format(timeFormatter))
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val dayOfWeek = trip.plannedTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru"))
                Text(
                    "${dayOfWeek.replaceFirstChar { it.uppercase() }}, ${trip.plannedTime.format(dateFormatter)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

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
                .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp))
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
