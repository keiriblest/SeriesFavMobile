package com.seriesfav.mobile.data.network

import com.seriesfav.mobile.data.model.Serie
import retrofit2.http.GET
import retrofit2.http.Url

interface GistApi {
    @GET
    suspend fun getSeries(@Url url: String): List<Serie>
}
