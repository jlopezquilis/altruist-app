package com.altruist.ui.theme

import androidx.compose.material3.MaterialTheme
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
    bodySmall = TextStyle(
        fontFamily = PublicSans,
        fontSize = 10.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PublicSans,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PublicSans,
        fontSize = 15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PublicSans,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    ),
    labelLarge = TextStyle(
        fontFamily = PublicSans,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleLarge = TextStyle(
        fontFamily = PublicSans,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
)

val ErrorTextStyle = TextStyle(
    fontFamily = PublicSans,
    fontSize = 14.sp
)

val TitleMediumTextStyle = TextStyle(
    fontFamily = PublicSans,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold
)

val BottomMenuTextStyle = TextStyle(
    fontFamily = PublicSans,
    fontSize = 12.sp
)

val TitleSmallTextStyle = TextStyle(
    fontFamily = PublicSans,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold
)


