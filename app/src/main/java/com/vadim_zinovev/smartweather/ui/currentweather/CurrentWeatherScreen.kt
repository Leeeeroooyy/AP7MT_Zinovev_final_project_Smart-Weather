package com.vadim_zinovev.smartweather.ui.currentweather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = viewModel()
) {
    val state = viewModel.uiState
    var cityQuery by remember { mutableStateOf("Zlin") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = cityQuery,
            onValueChange = { cityQuery = it },
            label = { Text("City name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val trimmed = cityQuery.trim()
                if (trimmed.isNotEmpty()) {
                    viewModel.loadWeatherForCity(trimmed)
                }
            }
        ) {
            Text("Load weather")
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
