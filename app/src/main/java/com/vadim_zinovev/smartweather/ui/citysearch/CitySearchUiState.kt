package com.vadim_zinovev.smartweather.ui.citysearch

import com.vadim_zinovev.smartweather.domain.model.City

data class CitySearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<City> = emptyList(),
    val errorMessage: String? = null
)
