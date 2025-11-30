package com.vadim_zinovev.smartweather.ui.currentweather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel,
    onSearchClick: () -> Unit
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = onSearchClick) {
            Text(text = "Search city")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.errorMessage != null -> {
                Text(text = "Error: ${state.errorMessage}")
            }

            state.temperatureText != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.cityName ?: "")
                    Text(text = state.temperatureText)
                    Text(text = state.description ?: "")
                }
            }

            else -> {
                Text(text = "No data")
            }
        }
    }
}
