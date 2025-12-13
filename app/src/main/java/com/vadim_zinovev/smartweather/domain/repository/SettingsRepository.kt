package com.vadim_zinovev.smartweather.domain.repository

import com.vadim_zinovev.smartweather.domain.model.TemperatureUnit
import kotlinx.coroutines.flow.Flow
import com.vadim_zinovev.smartweather.domain.model.AppTheme

interface SettingsRepository {
    fun observeTemperatureUnit(): Flow<TemperatureUnit>
    suspend fun setTemperatureUnit(unit: TemperatureUnit)
    fun observeAppTheme(): Flow<AppTheme>
    suspend fun setAppTheme(theme: AppTheme)
}