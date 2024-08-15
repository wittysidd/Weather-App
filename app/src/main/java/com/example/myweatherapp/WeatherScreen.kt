package com.example.myweatherapp

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myweatherapp.ui.theme.comfortaFont


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherScreen() {

    val weatherViewModel: MainViewModel = viewModel()
    val weather = weatherViewModel.weatherState.collectAsState().value
    val location = weatherViewModel.locationState.collectAsState().value

    val inputCity = remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF000000),
        targetValue = Color(0xFF009399),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color1"
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF009197),
        targetValue = Color(0xFF000000),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color 2"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(colors = listOf(color1, color2))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        val iconID = weather?.weather?.firstOrNull()?.icon
        val iconURL = "https://openweathermap.org/img/wn/$iconID.png"

        Image(painter = rememberAsyncImagePainter(iconURL),
            contentDescription = null,
            modifier = Modifier.size(90.dp) // Set a desired size for the image

        )
        if (weather != null) {
            val weatherDetails =
                weather.weather.firstOrNull()  // access the first element of the list or return null
            Text("Main: ${weatherDetails?.main}", modifier = Modifier.padding(10.dp), fontFamily = comfortaFont, color = Color.White)
            Text("Description : ${weatherDetails?.description}", fontFamily = comfortaFont, color = Color.White)
        }
        Text(text = "Enter City Name : ", modifier = Modifier.padding(top = 40.dp), fontFamily = comfortaFont, color = Color.White)

        OutlinedTextField(
            value = inputCity.value,
            onValueChange = { inputCity.value = it },
            textStyle = TextStyle(
                fontFamily = comfortaFont,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            label = { Text("City Name", color = Color.White) },
            singleLine = true,
            modifier = Modifier.size(350.dp, 75.dp),
            shape = RoundedCornerShape(40.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
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
            Text("Name: ${location.name}", modifier = Modifier.padding(10.dp), fontFamily = comfortaFont, color = Color.White)
        }

        if (weather?.loading == true) {
            // Show loading indicator
            Progress()
            Text(text = "Loading...", modifier = Modifier.padding(10.dp), fontFamily = comfortaFont, color = Color.White)
        } else if (weather?.error != null) {
            Text("Error Occured : ${weather.error}", modifier = Modifier.padding(6.dp), fontFamily = comfortaFont, color = Color.White)
        } else if (weather != null) {
            Text("Temp: ${weather.main.temp}", modifier = Modifier.padding(6.dp), color = colorOfTemp)
            Text("Feels Like : ${weather.main.feels_like}", modifier = Modifier.padding(6.dp), fontFamily = comfortaFont, color = Color.White)
            Text("Max Temp : ${weather.main.temp_max}", modifier = Modifier.padding(6.dp), fontFamily = comfortaFont, color = Color.White)
            Text("Humidity : ${weather.main.humidity}", modifier = Modifier.padding(6.dp), fontFamily = comfortaFont, color = Color.White)
        } else {
            Text(text = "No weather data available yet", modifier = Modifier.padding(6.dp), fontFamily = comfortaFont, color = Color.White)
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
