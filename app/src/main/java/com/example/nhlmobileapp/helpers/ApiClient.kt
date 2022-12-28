package com.example.nhlmobileapp.helpers

import okhttp3.RequestBody.Companion.toRequestBody
import com.example.nhlmobileapp.interfaces.ApiService
import com.example.nhlmobileapp.responses.LoginResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

class ApiClient(
    private val apiService: ApiService
) {
    suspend fun postLogin(username: String, password: String): SimpleResponse<LoginResponse> {
        return safeApiCall { apiService.loginUser(username, password) }
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