package com.seriesfav.mobile.data.model

import com.google.gson.annotations.SerializedName

data class Serie(
    @SerializedName("titulo")      val titulo: String         = "",
    @SerializedName("imgURL")      val imgURL: String         = "",
    @SerializedName("temporada")   val temporada: String      = "",
    @SerializedName("descripcion") val descripcion: String    = "",
    @SerializedName("plataforma")  val plataforma: String     = "",
    @SerializedName("seccion")     val seccion: String        = "",
    @SerializedName("fecha")       val fecha: String          = "",
    @SerializedName("trailerURL")  val trailerURL: String     = "",
    @SerializedName("verURL")      val verURL: List<Capitulo> = emptyList()
)

data class Capitulo(
    @SerializedName("temporada") val temporada: String = "",
    @SerializedName("capitulo")  val capitulo: String  = "",
    @SerializedName("url")       val url: String       = ""
)
