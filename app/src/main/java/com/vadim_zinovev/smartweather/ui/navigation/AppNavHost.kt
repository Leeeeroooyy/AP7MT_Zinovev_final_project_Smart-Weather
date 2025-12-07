package com.vadim_zinovev.smartweather.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vadim_zinovev.smartweather.ui.citydetail.CityDetailScreen
import com.vadim_zinovev.smartweather.ui.citysearch.CitySearchScreen
import com.vadim_zinovev.smartweather.ui.currentweather.CurrentWeatherScreen
import com.vadim_zinovev.smartweather.ui.currentweather.CurrentWeatherViewModel
import com.vadim_zinovev.smartweather.ui.currentweather.CurrentWeatherViewModelFactory
import com.vadim_zinovev.smartweather.ui.favorites.FavoritesScreen
import com.vadim_zinovev.smartweather.ui.settings.SettingsScreen
import com.vadim_zinovev.smartweather.ui.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"          // <-- стартуем со сплэша
    ) {
        // ---------- SPLASH ----------
        composable("splash") {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.CurrentWeather.route) {
                        popUpTo("splash") { inclusive = true } // убираем сплэш из back stack
                    }
                }
            )
        }

        // ---------- CURRENT WEATHER ----------
        composable(Screen.CurrentWeather.route) { backStackEntry ->
            val context = LocalContext.current
            val currentWeatherViewModel: CurrentWeatherViewModel = viewModel(
                factory = CurrentWeatherViewModelFactory(context)
            )

            val selectedCityName =
                backStackEntry.savedStateHandle.get<String>("selectedCityName")
            val selectedLat =
                backStackEntry.savedStateHandle.get<Double>("selectedLat")
            val selectedLon =
                backStackEntry.savedStateHandle.get<Double>("selectedLon")

            if (selectedCityName != null && selectedLat != null && selectedLon != null) {
                LaunchedEffect(selectedCityName, selectedLat, selectedLon) {
                    currentWeatherViewModel.loadWeatherForCityCoordinates(
                        cityName = selectedCityName,
                        latitude = selectedLat,
                        longitude = selectedLon
                    )
                    backStackEntry.savedStateHandle["selectedCityName"] = null
                    backStackEntry.savedStateHandle["selectedLat"] = null
                    backStackEntry.savedStateHandle["selectedLon"] = null
                }
            }

            CurrentWeatherScreen(
                viewModel = currentWeatherViewModel,
                onSearchClick = {
                    navController.navigate(Screen.CitySearch.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // ---------- CITY SEARCH ----------
        composable(Screen.CitySearch.route) {
            CitySearchScreen(
                onCitySelected = { city ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.apply {
                            set("selectedCityName", city.name)
                            set("selectedLat", city.latitude)
                            set("selectedLon", city.longitude)
                        }
                    navController.popBackStack()
                }
            )
        }

        // ---------- FAVORITES ----------
        composable(Screen.Favorites.route) {
            FavoritesScreen()
        }

        // ---------- SETTINGS ----------
        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        // ---------- CITY DETAIL ----------
        composable(Screen.CityDetail.route) { backStackEntry ->
            val cityId = backStackEntry.arguments
                ?.getString("cityId")
                ?.toLongOrNull() ?: -1L
            CityDetailScreen(cityId = cityId)
        }
    }
}
