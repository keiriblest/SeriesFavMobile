package com.seriesfav.mobile.data.repository

import com.seriesfav.mobile.data.model.Serie
import com.seriesfav.mobile.data.network.RetrofitInstance

private const val GIST_RAW_URL =
    "https://gist.githubusercontent.com/keiriblest/4634024c08cf2c794b4c6aa0b68ce7e8/raw/series-datos%20(1).json"

class SeriesRepository {
    suspend fun getSeries(): Result<List<Serie>> = runCatching {
        RetrofitInstance.api.getSeries(GIST_RAW_URL)
    }
}
