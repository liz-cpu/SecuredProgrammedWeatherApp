package com.example.nhlmobileapp.interfaces

import com.example.nhlmobileapp.responses.CoordinatesResponse
import com.example.nhlmobileapp.responses.LoginResponse
import com.example.nhlmobileapp.responses.ValidateTokenResponse
import com.example.nhlmobileapp.responses.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("user/register")
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("password1") password1: String,
        @Field("password2") password2: String
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @GET("user/validate-token")
    suspend fun validateToken(
    ): Response<ValidateTokenResponse>

    @Headers("Accept: application/json")
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("cnt") cnt: Int,
        @Query("appid") apiKey: String
    ): Response<WeatherForecastResponse>

    @Headers("Accept: application/json")
    @GET("geocode/v1/json")
    suspend fun getCoordsInfo(
        @Query("q") city: String,
        @Query("pretty") pretty: Int,
        @Query("key") apiKey: String
    ): Response<CoordinatesResponse>
}