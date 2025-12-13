package com.vadim_zinovev.smartweather.data.remote.dto

import com.squareup.moshi.Json

// Ответ /data/2.5/forecast
data class DailyForecastResponseDto(
    @Json(name = "list")
    val list: List<ForecastItemDto>
)

data class ForecastItemDto(
    @Json(name = "dt")
    val timestamp: Long,
    @Json(name = "main")
    val main: ForecastMainDto,
    @Json(name = "weather")
    val weather: List<WeatherDescriptionDto>
)

data class ForecastMainDto(
    @Json(name = "temp_min")
    val tempMin: Double,
    @Json(name = "temp_max")
    val tempMax: Double
)
