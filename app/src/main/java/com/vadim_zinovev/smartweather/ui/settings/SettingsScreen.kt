package com.vadim_zinovev.smartweather.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vadim_zinovev.smartweather.domain.model.AppTheme
import com.vadim_zinovev.smartweather.domain.model.TemperatureUnit

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )
    val uiState = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Temperature units",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        UnitOptionRow(
            label = "Celsius (°C)",
            isSelected = uiState.selectedUnit == TemperatureUnit.CELSIUS,
            onClick = { viewModel.onUnitSelected(TemperatureUnit.CELSIUS) }
        )

        UnitOptionRow(
            label = "Fahrenheit (°F)",
            isSelected = uiState.selectedUnit == TemperatureUnit.FAHRENHEIT,
            onClick = { viewModel.onUnitSelected(TemperatureUnit.FAHRENHEIT) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Theme",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        UnitOptionRow(
            label = "Light",
            isSelected = uiState.selectedTheme == AppTheme.LIGHT,
            onClick = { viewModel.onThemeSelected(AppTheme.LIGHT) }
        )

        UnitOptionRow(
            label = "Dark",
            isSelected = uiState.selectedTheme == AppTheme.DARK,
            onClick = { viewModel.onThemeSelected(AppTheme.DARK) }
        )
    }
}

@Composable
private fun UnitOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}
