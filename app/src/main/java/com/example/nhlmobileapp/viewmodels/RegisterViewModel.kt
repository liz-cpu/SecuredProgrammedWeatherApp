package com.example.nhlmobileapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.repositories.LoginRepository
import com.example.nhlmobileapp.repositories.RegisterRepository
import com.example.nhlmobileapp.responses.LoginResponse
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val repository: RegisterRepository = RegisterRepository()

    private val _registerLiveData = MutableLiveData<SimpleResponse<LoginResponse>>()
    val registerLiveData: LiveData<SimpleResponse<LoginResponse>> = _registerLiveData

    fun executeRegister(username: String, password1: String, password2: String) {

        viewModelScope.launch {
            val response: SimpleResponse<LoginResponse> = repository.register(username, password1, password2)

            _registerLiveData.postValue(response)
        }
    }
}