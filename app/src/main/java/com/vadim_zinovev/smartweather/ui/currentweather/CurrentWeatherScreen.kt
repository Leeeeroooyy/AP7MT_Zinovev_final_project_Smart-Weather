package com.vadim_zinovev.smartweather.ui.currentweather

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vadim_zinovev.smartweather.data.local.FavoritesStorage
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMyLocationClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val favoritesStorage = remember { FavoritesStorage(context.applicationContext) }
    val favoriteCities by favoritesStorage.favoriteCities.collectAsState(initial = emptyList())

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { max(favoriteCities.size, 1) }
    )

    var lastPage by remember { mutableStateOf(pagerState.currentPage) }

    LaunchedEffect(pagerState.currentPage, favoriteCities) {
        if (favoriteCities.isEmpty()) return@LaunchedEffect
        if (pagerState.currentPage != lastPage) {
            lastPage = pagerState.currentPage
            val city = favoriteCities.getOrNull(pagerState.currentPage) ?: return@LaunchedEffect
            viewModel.loadWeatherForCity(city)
        }
    }

    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF5B86E5),
                        Color(0xFF36D1DC)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                state.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.errorMessage}", color = Color.White)
                    }
                }

                state.temperatureText != null -> {
                    if (favoriteCities.isNotEmpty()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            WeatherCardContent(
                                state = state,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        WeatherCardContent(
                            state = state,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No data", color = Color.White)
                    }
                }
            }

            if (favoriteCities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                ) {
                    Text("Home")
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SmallFloatingActionButton(
                onClick = { menuExpanded = true },
                containerColor = Color(0xFF374785),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Search city") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    onClick = {
                        menuExpanded = false
                        onSearchClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Favorites") },
                    leadingIcon = { Icon(Icons.Default.Favorite, null) },
                    onClick = {
                        menuExpanded = false
                        onFavoritesClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings") },
                    leadingIcon = { Icon(Icons.Default.Settings, null) },
                    onClick = {
                        menuExpanded = false
                        onSettingsClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("My location") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    onClick = {
                        menuExpanded = false
                        onMyLocationClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun WeatherCardContent(
    state: CurrentWeatherUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.cityName.orEmpty(), style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))
            Text(state.temperatureText.orEmpty(), style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(state.description.orEmpty(), color = Color.White)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (state.minTempText != null || state.feelsLikeText != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.18f),
                        contentColor = Color.White
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Details", style = MaterialTheme.typography.titleMedium)
                        state.minTempText?.let { Text("min ${it}") }
                        state.maxTempText?.let { Text("max ${it}") }
                        state.feelsLikeText?.let { Text("feels like ${it}") }
                        state.humidity?.let { Text("humidity ${it}%") }
                        state.windSpeedText?.let { Text("wind ${it}") }
                        state.pressure?.let { Text("pressure ${it} hPa") }
                    }
                }
            }

            if (state.airQualityIndex != null && state.airQualityText != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.18f),
                        contentColor = Color.White
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Air quality", style = MaterialTheme.typography.titleMedium)
                        Text("AQI: ${state.airQualityIndex}")
                        Text(state.airQualityText)
                    }
                }
            }
        }
    }
}
