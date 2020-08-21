package com.musab.ad430

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class ForecastRepository {
    // CURRENT
    private val _currentForecast = MutableLiveData<DailyForecast>()
    val currentForecast: LiveData<DailyForecast> = _currentForecast

    // WEEKLY
    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>()
    val weeklyForecast: LiveData<List<DailyForecast>> = _weeklyForecast

    fun loadWeeklyForecast(zipCode: String) {
        val randomValues = List(7) { Random.nextFloat().rem(100) * 100 }

        val forecastItems = randomValues.map { temp ->
            // turn the random values to DailyForecast objects
            DailyForecast(temp, getTempDescription(temp))
        }

        _weeklyForecast.value = forecastItems
    }

    fun loadCurrentForecast(zipCode: String) {
        val randomTemp = Random.nextFloat().rem(100) * 100
        val forecast = DailyForecast(randomTemp, getTempDescription(randomTemp))
        _currentForecast.value = forecast

    }

    private fun getTempDescription(temp: Float) = when (temp) {
        in Float.MIN_VALUE.rangeTo(0f) -> "Invalid temp"
        in 0f.rangeTo(32f) -> "Way too cold"
        in 32f.rangeTo(55f) -> "Colder than i would prefer"
        in 55f.rangeTo(65f) -> "Getting better"
        in 65f.rangeTo(80f) -> "That's the sweet spot"
        in 80f.rangeTo(90f) -> "Getting a little warm"
        else -> "Does not compute."
    }
}