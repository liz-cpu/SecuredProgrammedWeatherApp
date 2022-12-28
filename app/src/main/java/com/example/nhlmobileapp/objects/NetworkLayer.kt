package com.example.nhlmobileapp.objects

import com.example.nhlmobileapp.helpers.ApiClient
import com.example.nhlmobileapp.helpers.CustomInterceptor
import com.example.nhlmobileapp.interfaces.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkLayer {
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(CustomInterceptor())
        .addInterceptor(interceptor)
        .build()

    private val okHttpClientExternal: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    //Start Custom API
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("http://192.168.111.48/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val apiClient = ApiClient(apiService)
    //End Custom API


    //Start OpenWeatherMap API

    private val retrofitOWM: Retrofit = Retrofit.Builder()
        .client(okHttpClientExternal)
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiServiceOWM: ApiService by lazy {
        retrofitOWM.create(ApiService::class.java)
    }

    val apiClientOWM = ApiClient(apiServiceOWM)
    //End OpenWeatherMap APO

    //Start OpenCageData API
    private val retrofitOCD: Retrofit = Retrofit.Builder()
        .client(okHttpClientExternal)
        .baseUrl("https://api.opencagedata.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiServiceOCD: ApiService by lazy {
        retrofitOCD.create(ApiService::class.java)
    }

    val apiClientOCD = ApiClient(apiServiceOCD)
    //End OpenCageData API
}