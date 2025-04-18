package com.altruist.ui.theme

import com.altruist.R

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val fontName = GoogleFont("Public Sans")
val PublicSans = FontFamily(
    Font(fontName, provider),
    Font(fontName, provider, FontWeight.Medium),
    Font(fontName, provider, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PublicSans,
        fontSize = 15.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PublicSans,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = PublicSans,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
)
