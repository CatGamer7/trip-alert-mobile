package com.example.tripalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripalert.di.appModule
import com.example.tripalert.ui.navigation.TripAlertNavGraph
import com.example.tripalert.ui.screens.triplist.TripListScreen
import com.example.tripalert.ui.theme.GreenPrimary
import com.example.tripalert.ui.theme.ThemeState
import com.example.tripalert.ui.theme.TripAlertTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            TripAlertTheme {
                TripAlertNavGraph()
            }
        }
    }
}




@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Шни шна шнапи шнапи шнапи шнап",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("plans") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Планы", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { ThemeState.isDarkTheme = !ThemeState.isDarkTheme },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (ThemeState.isDarkTheme) "Светлая тема" else "Темная тема",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
