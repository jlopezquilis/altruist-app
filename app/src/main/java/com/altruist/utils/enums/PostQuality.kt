package com.altruist.utils.enums

enum class PostQuality(val displayName: String) {
    BRAND_NEW("Nuevo"),
    ALMOST_NEW("Seminuevo"),
    NORMAL("Normal"),
    USED("Usado"),
    WIDELY_USED("Muy usado");

    companion object {
        // Devuelve una lista con los nombres de todos los estados
        val allQualities: List<String> = values().map { it.displayName }
    }
}