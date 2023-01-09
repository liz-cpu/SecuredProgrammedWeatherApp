package com.example.nhlmobileapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.repositories.DashboardRepository
import com.example.nhlmobileapp.responses.CoordinatesResponse
import com.example.nhlmobileapp.responses.WeatherForecastResponse
import kotlinx.coroutines.launch

class DashboardViewModel: ViewModel() {

    private val repository: DashboardRepository = DashboardRepository()

    private val _forecastLiveData = MutableLiveData<SimpleResponse<WeatherForecastResponse>>()
    val forecastLiveData: LiveData<SimpleResponse<WeatherForecastResponse>> = _forecastLiveData

    fun executeGetForecast(lat: Double, long: Double) {
        viewModelScope.launch {
            val response: SimpleResponse<WeatherForecastResponse> = repository.forecast(lat, long)

            _forecastLiveData.postValue(response)
        }
    }

    private val _coordsLiveData = MutableLiveData<SimpleResponse<CoordinatesResponse>>()
    val coordsLiveData: LiveData<SimpleResponse<CoordinatesResponse>> = _coordsLiveData

    fun executeGetCoords(city: String) {
        viewModelScope.launch {
            val response: SimpleResponse<CoordinatesResponse> = repository.coords(city)

            _coordsLiveData.postValue(response)
        }
    }
}