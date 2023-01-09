package com.example.nhlmobileapp.repositories

import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.objects.NetworkLayer
import com.example.nhlmobileapp.responses.LoginResponse
import com.example.nhlmobileapp.responses.ValidateTokenResponse


class LoginRepository {
    suspend fun login(username: String, password: String): SimpleResponse<LoginResponse> {
        return NetworkLayer.apiClient.postLogin(username, password)
    }

    suspend fun validateToken(): SimpleResponse<ValidateTokenResponse> {
        return NetworkLayer.apiClient.validateToken()
    }
}