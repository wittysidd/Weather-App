package com.example.myweatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {

    val weatherViewModel: MainViewModel = viewModel()
    val weather = weatherViewModel.weatherState.collectAsState().value
    val location = weatherViewModel.locationState.collectAsState().value

    val inputCity = remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        val iconID = weather?.weather?.firstOrNull()?.icon
        val iconURL = "https://openweathermap.org/img/wn/$iconID.png"

        Image(painter = rememberAsyncImagePainter(iconURL),
            contentDescription = null,
            modifier = Modifier.size(90.dp) // Set a desired size for the image

        )
        if (weather != null) {
            val weatherDetails =
                weather.weather.firstOrNull()  // access the first element of the list or return null
            Text("Main: ${weatherDetails?.main}", modifier = Modifier.padding(10.dp))
            Text("Description : ${weatherDetails?.description}")
        }
        Text(text = "Enter City Name : ", modifier = Modifier.padding(top = 40.dp))

        OutlinedTextField(
            value = inputCity.value,
            onValueChange = { inputCity.value = it },
            modifier = Modifier.padding(10.dp)
        )

        Button(
            onClick = {
                weatherViewModel.fetchWeatherDetails(inputCity.value)
                weather?.loading = true
                keyboardController?.hide()

            },
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text("How's Weather ? ")
        }

        var colorOfTemp : Color = Color.White
        if(weather?.main?.temp != null) {
            if(weather.main.temp > 30.0)
            {
                colorOfTemp = Color.Red
            }
            else if(weather.main.temp < 30.0 && weather.main.temp > 8.0){
                colorOfTemp = Color.Green
            }
            else if(weather.main.temp < 8.0 ){
                colorOfTemp = Color.Blue
            }
            else{
                colorOfTemp = Color.White
            }
        }

        if (location != null) {
            Text("Name: ${location.name}", modifier = Modifier.padding(10.dp))
        }

        if (weather?.loading == true) {
            // Show loading indicator
            Progress()
            Text(text = "Loading...", modifier = Modifier.padding(10.dp))
        } else if (weather?.error != null) {
            Text("Error Occured : ${weather.error}", modifier = Modifier.padding(6.dp))
        } else if (weather != null) {
            Text("Temp: ${weather.main.temp}", modifier = Modifier.padding(6.dp), color = colorOfTemp)
            Text("Feels Like : ${weather.main.feels_like}", modifier = Modifier.padding(6.dp))
            Text("Max Temp : ${weather.main.temp_max}", modifier = Modifier.padding(6.dp))
            Text("Humidity : ${weather.main.humidity}", modifier = Modifier.padding(6.dp))
        } else {
            Text(text = "No weather data available yet", modifier = Modifier.padding(6.dp))
        }
    }
}

@Composable
fun Progress(modifier : Modifier = Modifier){
    CircularProgressIndicator(modifier = modifier)
}


//
//
//
//
// {
//
//    val weatherViewModel: MainViewModel = viewModel()
//    val weather by weatherViewModel.weatherState
//    //val Location by weatherViewModel.locationState
//    val inputCity : String = ""
//
//    Column {
//
//
//        Text(text = "Enter City Name : ", modifier = Modifier.padding(10.dp))
//
//        OutlinedTextField(
//            value = inputCity,
//            onValueChange = { inputCity = it },
//            modifier = Modifier.padding(10.dp)
//        )
//
//        Button(
//            onClick = {
//                weatherViewModel.fetchWeatherDetails(inputCity)
//            },
//            modifier = Modifier.padding(10.dp)
//        ) {
//            Text("How's Weather ? ")
//        }
//
////        Text(text = "Name = ${Location.name}")
////        Text(text = "Longitude = ${Location.lon}")
////        Text(text = "Latitude = ${Location.lat}")
//
//        if (weather?.loading == true) {
//            Text(text = "Loading")
//        }
//        else if (weather?.error != null)
//        {
//            Text("Error Occured : ${weather?.error}")
//        }
//        else {
//            Text("Main: ${weather?.main}")
//            Text("Desc: ${weather?.description}")
//            Text("Temp: ${weather?.temp}")
//        }
//    }
//}
//
