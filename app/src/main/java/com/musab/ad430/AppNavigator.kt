package com.musab.ad430

interface AppNavigator {
    fun navigateToCurrentForecast(zipCode: String)
    fun navigateToLocationEntry()
    fun navigateToForecastDetails(forecast: DailyForecast)
}