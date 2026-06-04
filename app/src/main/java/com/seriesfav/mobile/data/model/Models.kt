package com.seriesfav.mobile.data.model

data class Serie(
    val titulo: String = "",
    val imgURL: String = "",
    val temporada: String = "",
    val descripcion: String = "",
    val plataforma: String = "",
    val seccion: String = "",
    val fecha: String = "",
    val trailerURL: String = "",
    val verURL: List<VerItem> = emptyList(),
    val descargas: List<VerItem> = emptyList()
)

data class VerItem(
    val temporada: String = "",
    val capitulo: String = "",
    val url: String = ""
)
