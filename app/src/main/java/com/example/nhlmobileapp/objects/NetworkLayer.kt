package com.example.nhlmobileapp.objects

import com.example.nhlmobileapp.helpers.ApiClient
import com.example.nhlmobileapp.helpers.CustomInterceptor
import com.example.nhlmobileapp.interfaces.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

object NetworkLayer {
    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(CustomInterceptor())
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("http://localhost")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val apiClient = ApiClient(apiService)

}