package com.weather.ksy.service

import com.weather.ksy.model.TotalNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("/v2/top-headlines?country=kr")
    fun getNews(
        @Query("category") category:String,
        @Query("apiKey") apiKey:String
    )
    :Call<TotalNews>

    @GET("v2/top-headlines?country=kr")
    fun searchNews(
        @Query("q") query:String,
        @Query("apiKey") apiKey:String
    ):Call<TotalNews>
}