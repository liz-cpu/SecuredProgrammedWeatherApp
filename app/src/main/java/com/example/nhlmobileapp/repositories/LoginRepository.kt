package com.example.nhlmobileapp.repositories

import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.objects.NetworkLayer
import com.example.nhlmobileapp.responses.LoginResponse


class LoginRepository {
    suspend fun login(username: String, password: String): SimpleResponse<LoginResponse> {
        val request = NetworkLayer.apiClient.postLogin(username, password)
        return request
    }
}