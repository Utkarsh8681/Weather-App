package com.example.weather.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("Key")api : String,
        @Query("q") city : String
    ) : Response<WeatherModel>
}