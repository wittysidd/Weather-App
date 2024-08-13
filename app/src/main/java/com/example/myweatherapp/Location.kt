package com.example.myweatherapp

data class Location(val name: String,
                    val lat: Double,
                    val lon: Double,
                    val country: String,
                    val state: String? // Optional field, might be null
)
