package com.example.nhlmobileapp.repositories

import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.objects.NetworkLayer
import com.example.nhlmobileapp.responses.CoordinatesResponse
import com.example.nhlmobileapp.responses.WeatherForecastResponse

class DashboardRepository {
    suspend fun forecast(
        lat: Double,
        long: Double
    ): SimpleResponse<WeatherForecastResponse> {
        return NetworkLayer.apiClientOWM.getForecast(lat, long)
    }

    suspend fun coords(
        city: String
    ): SimpleResponse<CoordinatesResponse> {
        return NetworkLayer.apiClientOCD.getCoords(city)
    }
}