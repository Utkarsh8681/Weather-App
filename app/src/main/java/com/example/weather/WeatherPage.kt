package com.example.weather

import android.R
import android.widget.Toast
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.WeatherViewModel
import com.example.weather.api.NetworkResponse
import com.example.weather.api.WeatherModel

@Composable
fun WeatherPage(viewModel : WeatherViewModel) {
    var city by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val weatherResult = viewModel.weatherResult.observeAsState()
    val onSearchClick: () -> Unit = {
         viewModel.getData(city)
        keyboardController?.hide()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(value = city,
                singleLine = true,
                onValueChange = {city = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClick()
                        keyboardController?.hide()
                    }
                ),
                label = { Text(text = "City Name")})
            IconButton(onClick = { onSearchClick() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription ="Search" , Modifier.size(45.dp))
            }

        }
        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(result.data)
//            Text(text = result.data.toString())
            }
            null -> {}
        }
    }

}

@Composable
fun WeatherDetails(data : WeatherModel){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
        ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(imageVector = Icons.Default.LocationOn, contentDescription ="Location" , Modifier.size(35.dp))
            Text(text = "${data.location.name}, ${data.location.country}", style = MaterialTheme.typography.headlineMedium)
//            Text(text = ",",style = MaterialTheme.typography.headlineMedium)
//            Text(text = data.location.country, style = MaterialTheme.typography.headlineMedium)
        }
        Text(text = " ${data.current.temp_c}℃ ", fontSize = 60.sp , fontWeight = FontWeight.Bold, modifier = Modifier)
        AsyncImage(model = "https:${data.current.condition.icon}"
            .replace("64x64","128x128")
            , contentDescription ="Icon" ,
            modifier = Modifier.size(150.dp)
        )
//        Icon(imageVector = , contentDescription ="Weather Icon" , modifier = Modifier.size(150.dp) )
        Text(text = data.current.condition.text , Modifier.padding(top = 20.dp), fontSize = 20.sp)
        Card (
            modifier = Modifier
                .size(400.dp, 250.dp)
                .padding(top = 30.dp)
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Temperature(condition = "Humidity", value = data.current.humidity.toString())
                    Temperature(condition = "Cloud", value = data.current.cloud.toString())
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Temperature(condition = "UV rays", value = data.current.uv)
                    Temperature(condition = "Wind Speed", value = data.current.wind_kph)

                }
            }
        }
    }
}

@Composable
fun Temperature(
    condition : String,
    value : String
) {

    Card(modifier = Modifier.wrapContentSize()) {
        Column (
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = value, fontSize = 30.sp , fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = condition, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun wet() {
//    WeatherDetails(data = )
}
@Composable
fun rememberRainy(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "rainy",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(23.208f, 36.458f)
                quadToRelative(-0.458f, 0.25f, -1f, 0.063f)
                quadToRelative(-0.541f, -0.188f, -0.791f, -0.646f)
                lineToRelative(-2.75f, -5.5f)
                quadToRelative(-0.25f, -0.5f, -0.084f, -1.042f)
                quadToRelative(0.167f, -0.541f, 0.667f, -0.791f)
                quadToRelative(0.5f, -0.209f, 1.021f, -0.042f)
                quadToRelative(0.521f, 0.167f, 0.771f, 0.667f)
                lineToRelative(2.75f, 5.5f)
                quadToRelative(0.25f, 0.5f, 0.083f, 1.021f)
                quadToRelative(-0.167f, 0.52f, -0.667f, 0.77f)
                close()
                moveToRelative(10f, 0f)
                quadToRelative(-0.458f, 0.209f, -1f, 0.042f)
                quadToRelative(-0.541f, -0.167f, -0.791f, -0.667f)
                lineToRelative(-2.75f, -5.5f)
                quadToRelative(-0.25f, -0.5f, -0.084f, -1.021f)
                quadToRelative(0.167f, -0.52f, 0.667f, -0.77f)
                reflectiveQuadToRelative(1.021f, -0.063f)
                quadToRelative(0.521f, 0.188f, 0.771f, 0.646f)
                lineToRelative(2.75f, 5.5f)
                quadToRelative(0.25f, 0.5f, 0.083f, 1.042f)
                quadToRelative(-0.167f, 0.541f, -0.667f, 0.791f)
                close()
                moveToRelative(-20f, 0f)
                quadToRelative(-0.458f, 0.209f, -1f, 0.042f)
                quadToRelative(-0.541f, -0.167f, -0.791f, -0.625f)
                lineToRelative(-2.75f, -5.5f)
                quadToRelative(-0.25f, -0.5f, -0.063f, -1.042f)
                quadToRelative(0.188f, -0.541f, 0.688f, -0.791f)
                quadToRelative(0.458f, -0.209f, 1f, -0.042f)
                quadToRelative(0.541f, 0.167f, 0.791f, 0.625f)
                lineToRelative(2.75f, 5.542f)
                quadToRelative(0.25f, 0.5f, 0.063f, 1.021f)
                quadToRelative(-0.188f, 0.52f, -0.688f, 0.77f)
                close()
                moveToRelative(-1f, -10.416f)
                quadToRelative(-3.625f, 0f, -6.208f, -2.584f)
                quadToRelative(-2.583f, -2.583f, -2.583f, -6.25f)
                quadToRelative(0f, -3.291f, 2.312f, -5.875f)
                quadTo(8.042f, 8.75f, 11.5f, 8.458f)
                quadToRelative(1.333f, -2.333f, 3.562f, -3.708f)
                quadTo(17.292f, 3.375f, 20f, 3.375f)
                quadToRelative(3.75f, 0f, 6.375f, 2.396f)
                reflectiveQuadToRelative(3.208f, 5.979f)
                quadToRelative(3.125f, 0.167f, 5.084f, 2.25f)
                quadToRelative(1.958f, 2.083f, 1.958f, 4.875f)
                quadToRelative(0f, 2.958f, -2.104f, 5.063f)
                quadToRelative(-2.104f, 2.104f, -5.063f, 2.104f)
                close()
                moveToRelative(0f, -2.667f)
                horizontalLineToRelative(17.25f)
                quadToRelative(1.875f, 0f, 3.188f, -1.313f)
                quadToRelative(1.312f, -1.312f, 1.312f, -3.187f)
                quadToRelative(0f, -1.875f, -1.312f, -3.187f)
                quadToRelative(-1.313f, -1.313f, -3.188f, -1.313f)
                horizontalLineToRelative(-2.416f)
                verticalLineToRelative(-1.333f)
                quadToRelative(0f, -2.917f, -2.063f, -4.959f)
                quadTo(22.917f, 6.042f, 20f, 6.042f)
                quadToRelative(-2.125f, 0f, -3.875f, 1.125f)
                reflectiveQuadToRelative(-2.583f, 3.083f)
                lineToRelative(-0.334f, 0.792f)
                horizontalLineToRelative(-1.083f)
                quadToRelative(-2.542f, 0.083f, -4.313f, 1.875f)
                quadToRelative(-1.77f, 1.791f, -1.77f, 4.291f)
                quadToRelative(0f, 2.584f, 1.812f, 4.375f)
                quadToRelative(1.813f, 1.792f, 4.354f, 1.792f)
                close()
                moveTo(20f, 14.708f)
                close()
            }
        }.build()
    }
}