package com.altruist.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgoText(isoDateTimeString: String): String {
    return try {
        val publishedTime = OffsetDateTime.parse(isoDateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val now = OffsetDateTime.now()
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
