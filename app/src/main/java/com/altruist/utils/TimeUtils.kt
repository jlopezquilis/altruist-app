package com.altruist.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgoText(isoDateTimeString: String): String {
    return try {
        val publishedTime = LocalDateTime.parse(isoDateTimeString)
            .atOffset(ZoneOffset.UTC)  // lo marcas explícitamente como UTC
            .toZonedDateTime()

        val now = ZonedDateTime.now() // en la zona horaria del móvil

        val duration = Duration.between(publishedTime, now)


        when {
            duration.toMinutes() < 1 -> "Publicado hace unos segundos"
            duration.toMinutes() < 60 -> "Publicado hace ${duration.toMinutes()} min"
            duration.toHours() < 24 -> "Publicado hace ${duration.toHours()} h"
            else -> "Publicado hace ${duration.toDays()} días"
        }
    } catch (e: Exception) {
        "Publicado hace poco"
    }
}
