package com.weather.ksy.service

import com.weather.ksy.model.TotalWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("/data/2.5/weather/")
    fun getWeatherInfoOfLocation(
        @Query("q" ) location: String,
        @Query("APPID") appID: String
    ): Call<TotalWeather>

    @GET("/data/2.5/weather/")
    fun getWeatherInfoOfCoordinates(
        @Query("lat") latitude : Double,
        @Query("lon") logitude : Double,
        @Query("APPID") appID : String,
        @Query("units") units : String,
        @Query("lang") language : String
    ): Call<TotalWeather>

}