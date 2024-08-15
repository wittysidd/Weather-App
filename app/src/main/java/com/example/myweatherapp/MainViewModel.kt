package com.example.myweatherapp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherData?>(WeatherData(MainObject(null,null,null,null,null), emptyList()))
    val weatherState: StateFlow<WeatherData?> = _weatherState.asStateFlow()

    private var _locationState = MutableStateFlow<Location?>(Location("", 0.0,0.0, "", ""))
    val locationState: StateFlow<Location?> = _locationState.asStateFlow()

    private val apiKey = "API KEY" // Replace with your actual API key

    fun fetchWeatherDetails(cityName: String) {
        viewModelScope.launch {
            try {
                val locationResponse = ApiClient.apiService.getLocationCords(cityName = cityName, apiKey = apiKey)
                val location = locationResponse.getOrNull(0)
                if (location != null) {
                    _locationState.value = location.copy()
                }
                if (location != null) {
                    val weatherResponse = ApiClient.apiService.getWeatherByCoords(
                        latitude = location.lat,
                        longitude = location.lon,
                        apiKey = apiKey
                    )

                    _weatherState.value = weatherResponse.copy(
                        loading = false
                    )
                } else {
                    _locationState.value = _locationState.value?.copy(name = cityName)

                    _weatherState.value = _weatherState.value?.copy(
                        loading = false,
                        error = "Location not found for city: $cityName"
                    )
                }
            } catch (e: Exception) {
                _weatherState.value = _weatherState.value?.copy(
                    loading = false,
                    error = "Error fetching weather: ${e.message}"
                )
            }
        }
    }
}
