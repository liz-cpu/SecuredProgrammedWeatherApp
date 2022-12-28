package com.example.nhlmobileapp.interfaces

import com.example.nhlmobileapp.helpers.CustomBody
import com.example.nhlmobileapp.responses.LoginResponse
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("user")
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>
}