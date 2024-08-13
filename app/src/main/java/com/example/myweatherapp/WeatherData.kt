package com.example.myweatherapp

data class WeatherData(
    val main: MainObject, // Main weather condition (e.g., Clouds)
    val weather : List<WeatherObject>,
    var loading: Boolean = false,
    val error: String? = null   // question mark makes it nullable

)

data class MainObject(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val humidity: Double?
)

data class WeatherObject(
    val main: String?,
    val description: String?,
    val icon: String
)