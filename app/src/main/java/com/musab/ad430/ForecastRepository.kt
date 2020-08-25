package com.musab.ad430

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.musab.ad430.api.CurrentWeather
import com.musab.ad430.api.WeeklyForecast
import com.musab.ad430.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository {
    // CURRENT
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    // WEEKLY
    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast: LiveData<WeeklyForecast> = _weeklyForecast

    fun loadWeeklyForecast(zipCode: String) {
        val call = createOpenWeatherMapService().currentWeather(
            zipCode,
            "imperial",
            BuildConfig.OPEN_WEATHER_API_KEY
        )
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(
                    ForecastRepository::class.java.simpleName,
                    "Error loading location for weekly forecast",
                    t
                )
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    // load 7 day forecast
                    val call = createOpenWeatherMapService().sevenDayForecast(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        exclude = "current,minutely,hourly",
                        apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
                        units = "imperial"
                    )

                    call.enqueue(object : Callback<WeeklyForecast> {
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(
                                ForecastRepository::class.java.simpleName,
                                "Error loading weekly forecast",
                                t
                            )
                        }

                        override fun onResponse(
                            call: Call<WeeklyForecast>,
                            response: Response<WeeklyForecast>
                        ) {
                            val weeklyResponse = response.body()
                            if (weeklyResponse != null) {
                                _weeklyForecast.value = weeklyResponse
                            }
                        }

                    })
                }
            }

        })
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


}