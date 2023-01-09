package com.example.nhlmobileapp.repositories

import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.objects.NetworkLayer
import com.example.nhlmobileapp.responses.LoginResponse

class RegisterRepository {
    suspend fun register(username: String, password1: String, password2: String): SimpleResponse<LoginResponse> {
        val request = NetworkLayer.apiClient.postRegister(username, password1, password2)
        return request
    }
}