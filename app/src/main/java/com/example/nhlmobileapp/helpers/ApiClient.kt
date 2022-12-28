package com.example.nhlmobileapp.helpers

import com.example.nhlmobileapp.MainActivity
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.nhlmobileapp.interfaces.ApiService
import com.example.nhlmobileapp.responses.CoordinatesResponse
import com.example.nhlmobileapp.responses.LoginResponse
import com.example.nhlmobileapp.responses.ValidateTokenResponse
import com.example.nhlmobileapp.responses.WeatherForecastResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

class ApiClient(
    private val apiService: ApiService,
) {
    suspend fun postLogin(username: String, password: String): SimpleResponse<LoginResponse> {
        return safeApiCall { apiService.loginUser(username, password) }
    }

    suspend fun validateToken(): SimpleResponse<ValidateTokenResponse> {
        return safeApiCall { apiService.validateToken() }
    }

    suspend fun postRegister(username: String, password1: String, password2: String): SimpleResponse<LoginResponse> {
        return safeApiCall { apiService.registerUser(username, password1, password2) }
    }

    suspend fun getForecast(lat: Double, long: Double): SimpleResponse<WeatherForecastResponse> {
        return safeApiCall { apiService.getForecast(lat, long, "metric", "nl", 7, CryptoManager("owmKey").decrypt(MainActivity.fileDirectory)) }
    }

    suspend fun getCoords (city: String): SimpleResponse<CoordinatesResponse> {
        return safeApiCall { apiService.getCoordsInfo(city, 1, "5bc7cb72cab74083905f31c4ac3baee3") }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            val response: Response<T> = apiCall.invoke()
            if(response.code().equals(200)) SimpleResponse.success(response) else SimpleResponse.error(response)
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
    }
}