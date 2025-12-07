package com.vadim_zinovev.smartweather.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onCitySelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(context)
    )

    val favorites by viewModel.favoriteCities.collectAsState()
    var newCityName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite cities") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onSearchClick) {
                    Text("Search with suggestions")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Add city manually") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val city = newCityName.trim()
                    if (city.isNotEmpty()) {
                        viewModel.onToggleFavorite(city)
                        newCityName = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add to favorites")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (favorites.isEmpty()) {
                Text(
                    text = "No favorite cities yet",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favorites) { cityName ->
                        FavoriteCityRow(
                            cityName = cityName,
                            onClick = { onCitySelected(cityName) },
                            onRemoveClick = { viewModel.onRemoveCity(cityName) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteCityRow(
    cityName: String,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = cityName,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        TextButton(onClick = onRemoveClick) {
            Text("Remove")
        }
    }
}
