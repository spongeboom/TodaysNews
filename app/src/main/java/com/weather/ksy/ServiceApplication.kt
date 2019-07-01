package com.weather.ksy

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.weather.ksy.service.NewsAPI
import com.weather.ksy.service.WeatherAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceApplication : Application(){

    var weatherAPI: WeatherAPI? = null
    var newsApi: NewsAPI? = null

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        setupRetrofit()
    }

    private fun setupRetrofit(){
        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor(StethoInterceptor())
        val client = httpClient.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        weatherAPI = retrofit.create(WeatherAPI::class.java)

        val newsretrofit:Retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        newsApi = newsretrofit.create(NewsAPI::class.java)
    }

    fun requestService(): WeatherAPI?{
        return weatherAPI
    }

    fun requestNews(): NewsAPI? {
        return newsApi
    }

}