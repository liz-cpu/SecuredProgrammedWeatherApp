package com.example.nhlmobileapp.viewmodels

import androidx.lifecycle.*
import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.repositories.LoginRepository
import com.example.nhlmobileapp.responses.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val repository: LoginRepository = LoginRepository()

    private val _loginLiveData = MutableLiveData<SimpleResponse<LoginResponse>>()
    val loginLiveData: LiveData<SimpleResponse<LoginResponse>> = _loginLiveData

    fun executeLogin(username: String, password: String) {

        viewModelScope.launch {
            val response: SimpleResponse<LoginResponse> = repository.login(username, password)

            _loginLiveData.postValue(response)
        }
    }
}