package com.musab.ad430

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.musab.ad430.api.CurrentWeather
import com.musab.ad430.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class ForecastRepository {
    // CURRENT
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

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

        val call = createOpenWeatherMapService().currentWeather(
            zipCode,
            "imperial",
            BuildConfig.OPEN_WEATHER_API_KEY
        )



        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "Error loading current data", t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    _currentWeather.value = weatherResponse
                }
            }

        })

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