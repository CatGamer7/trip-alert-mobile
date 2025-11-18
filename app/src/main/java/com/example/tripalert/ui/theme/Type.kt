package com.example.tripalert.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tripalert.R


val AnonymousPro = FontFamily(
    Font(R.font.anonymous_pro_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.anonymous_pro_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.anonymous_pro_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.anonymous_pro_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)