package com.vadim_zinovev.smartweather.domain.repository

import com.vadim_zinovev.smartweather.domain.model.AirQuality
import com.vadim_zinovev.smartweather.domain.model.Weather

interface WeatherRepository {

    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Weather

    suspend fun getCurrentWeatherByCityName(
        cityName: String
    ): Weather

    suspend fun getAirQualityByCoordinates(
        latitude: Double,
        longitude: Double
    ): AirQuality?
}
