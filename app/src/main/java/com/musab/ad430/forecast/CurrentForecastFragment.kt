package com.musab.ad430.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.musab.ad430.*
import com.musab.ad430.api.CurrentWeather

class CurrentForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager
    private lateinit var locationRepository: LocationRepository
    private val forecastRepository = ForecastRepository()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingsManager = TempDisplaySettingsManager(requireContext())


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)
        val locationName = view.findViewById<TextView>(R.id.locationName)
        val tempText = view.findViewById<TextView>(R.id.tempText)
        val fab: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        fab.setOnClickListener {
            showLocationEntry()
        }
        // create the observer
        val currentWeatherObserver = Observer<CurrentWeather> { weather ->
            // update our list adapter
            locationName.text = weather.name
            tempText.text = formatTempForDisplay(
                weather.forecast.temp,
                tempDisplaySettingsManager.getTempDisplaySetting()
            )
        }

        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    forecastRepository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }

    private fun showLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment2()

        findNavController().navigate(action)
    }


}
