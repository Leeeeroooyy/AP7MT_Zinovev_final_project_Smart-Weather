package com.vadim_zinovev.smartweather

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.vadim_zinovev.smartweather.data.repository.SettingsRepositoryImpl
import com.vadim_zinovev.smartweather.domain.model.AppTheme
import com.vadim_zinovev.smartweather.ui.navigation.AppNavHost
import com.vadim_zinovev.smartweather.ui.theme.SmartWeatherTheme

class MainActivity : ComponentActivity() {

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermissions()

        setContent {
            val context = LocalContext.current.applicationContext
            val settingsRepository = SettingsRepositoryImpl(context)

            val appTheme by settingsRepository.observeAppTheme()
                .collectAsState(initial = AppTheme.LIGHT)

            SmartWeatherTheme(appTheme = appTheme) {
                AppNavHost()
            }
        }
    }

    private fun requestLocationPermissions() {
        val fineGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        if (!fineGranted || !coarseGranted) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}
