package com.altruist.utils.enums

enum class PostStatus(val displayName: String) {
    NUEVO("Nuevo"),
    SEMINUEVO("Seminuevo"),
    NORMAL("Normal"),
    USADO("Usado"),
    MUY_USADO("Muy usado");

    companion object {
        // Devuelve una lista con los nombres de todos los estados
        val allStatuses: List<String> = values().map { it.displayName }
    }
}