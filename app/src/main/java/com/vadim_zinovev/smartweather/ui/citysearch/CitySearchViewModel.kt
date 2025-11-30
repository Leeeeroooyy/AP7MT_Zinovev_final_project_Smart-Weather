package com.vadim_zinovev.smartweather.ui.citysearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadim_zinovev.smartweather.data.remote.api.WeatherApiFactory
import com.vadim_zinovev.smartweather.data.repository.CityRepositoryImpl
import com.vadim_zinovev.smartweather.domain.repository.CityRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CitySearchViewModel : ViewModel() {

    private val cityRepository: CityRepository =
        CityRepositoryImpl(WeatherApiFactory.create())

    var uiState by mutableStateOf(CitySearchUiState())
        private set

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        uiState = uiState.copy(query = newQuery)

        searchJob?.cancel()

        val trimmed = newQuery.trim()

        if (trimmed.length < 2) {
            uiState = uiState.copy(
                isLoading = false,
                results = emptyList(),
                errorMessage = null
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // debounce 0.5 сек
            performSearch(trimmed, fromTyping = true)
        }
    }

    fun search() {
        val q = uiState.query.trim()
        if (q.isEmpty()) return

        searchJob?.cancel()

        viewModelScope.launch {
            performSearch(q, fromTyping = false)
        }
    }

    private suspend fun performSearch(query: String, fromTyping: Boolean) {
        uiState = uiState.copy(
            isLoading = !fromTyping,
            errorMessage = null
        )

        try {
            val cities = cityRepository.searchCitiesByName(query)
            uiState = uiState.copy(
                isLoading = false,
                results = cities
            )
        } catch (e: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                results = emptyList(),
                errorMessage = if (fromTyping) null
                else e.message ?: "Fail to find the city"
            )
        }
    }
}
