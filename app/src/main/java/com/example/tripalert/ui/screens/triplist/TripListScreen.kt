package com.example.tripalert.ui.screens.triplist

import android.R.attr.fontWeight
import com.example.tripalert.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripalert.ui.theme.*


@Composable
fun TripListScreen(
    navController: NavHostController,
    onAddClick: () -> Unit,
    onTripClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyTrips = listOf(1L, 2L, 3L)
    val otherTrips = listOf(4L, 5L)

    Scaffold(
        topBar = { DailyTripsTopBar() },
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

        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // --- Ежедневные ---
            if (dailyTrips.isNotEmpty()) {
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

                items(dailyTrips.size) { index ->
                    TripCard(
                        onClick = { onTripClick(dailyTrips[index]) }
                    )
                }
            }

            // --- Другие ---
            if (otherTrips.isNotEmpty()) {
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

                items(otherTrips.size) { index ->
                    TripCard(
                        onClick = { onTripClick(otherTrips[index]) }
                    )
                }
            }
        }
    }
}


@Composable
fun DailyTripsTopBar() {
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
fun TripCard(onClick: () -> Unit) {
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
                        "Ул.Вершинина д.39а",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.runman),
                        contentDescription = null,
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

            // Время
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeLabel("Время", "12:40")

                Icon(
                    painter = painterResource(id = R.drawable.minus_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .offset(y = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                TimeLabel("Время в пути", "00:17")

                Icon(
                    painter = painterResource(id = R.drawable.equal_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .offset(y = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                TimeLabel("Выход в", "12:23")
            }

            Spacer(Modifier.height(16.dp))

            // Подвал
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Среда, 22.10.2025",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    "Из: Дом",
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