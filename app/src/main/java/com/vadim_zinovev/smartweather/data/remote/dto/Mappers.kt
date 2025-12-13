package com.vadim_zinovev.smartweather.data.remote.dto

import com.vadim_zinovev.smartweather.domain.model.AirQuality
import com.vadim_zinovev.smartweather.domain.model.City
import com.vadim_zinovev.smartweather.domain.model.Weather
import com.vadim_zinovev.smartweather.domain.model.DailyForecast

fun CurrentWeatherResponseDto.toDomainWeather(): Weather {
    val firstWeather = weather.firstOrNull()
    return Weather(
        temperature = main.temperature,
        feelsLike = main.feelsLike,
        minTemperature = main.tempMin,
        maxTemperature = main.tempMax,
        description = firstWeather?.description ?: "",
        iconCode = firstWeather?.icon ?: "",
        humidity = main.humidity,
        pressure = main.pressure,
        windSpeed = wind.speed,
        timestamp = timestamp,
        latitude = coord.lat,
        longitude = coord.lon,
        cityName = name,
    )
}

fun AirQualityResponseDto.toDomainAirQuality(): AirQuality? {
    val item = list.firstOrNull() ?: return null
    val aqiValue = item.main.aqi

    return AirQuality(
        index = aqiValue,
        aqi = aqiValue,
        pm25 = item.components.pm25,
        pm10 = item.components.pm10,
        o3 = item.components.o3
    )
}

fun CityGeocodingDto.toDomainCity(
    id: Long = 0L,
    isFavorite: Boolean = false
): City = City(
    id = id,
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude,
    isFavorite = isFavorite
)

fun DailyForecastResponseDto.toDomainDailyForecast(): List<DailyForecast> {
    if (list.isEmpty()) return emptyList()
    val itemsByDay = list.groupBy { item ->
        item.timestamp / 86_400L   // 60 * 60 * 24
    }
    return itemsByDay
        .toSortedMap()
        .values
        .map { dayItems ->
            val first = dayItems.first()
            val minTemp = dayItems.minOf { it.main.tempMin }
            val maxTemp = dayItems.maxOf { it.main.tempMax }

            val description = dayItems
                .mapNotNull { it.weather.firstOrNull()?.description }
                .firstOrNull() ?: ""

            DailyForecast(
                timestamp = first.timestamp,
                minTemp = minTemp,
                maxTemp = maxTemp,
                description = description
            )
        }
}
