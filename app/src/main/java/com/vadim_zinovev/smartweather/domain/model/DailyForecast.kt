package com.vadim_zinovev.smartweather.domain.model

data class DailyForecast(
    val timestamp: Long,
    val minTemp: Double,
    val maxTemp: Double,
    val description: String
)
