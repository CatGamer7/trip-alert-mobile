package com.example.tripalert.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Typography
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat


val AnonTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = AnonymousPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AnonymousPro,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AnonymousPro,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AnonymousPro,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = AnonymousPro,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
)


private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = LightTextPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = GreyBackground,
    onSurface = LightTextPrimary,
    secondary = LogoAva,
    tertiary = TextGreyBack
)

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = GreyBackgroundDark,
    onSurface = DarkTextPrimary,
    secondary = LogoAvaDark,
    tertiary = TextGreyBackDark
)

@Composable
fun TripAlertTheme(
    darkTheme: Boolean = ThemeState.isDarkTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AnonTypography,
        content = content
    )
}