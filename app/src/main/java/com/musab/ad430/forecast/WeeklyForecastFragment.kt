package com.musab.ad430.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.musab.ad430.*
import com.musab.ad430.api.DailyForecast
import com.musab.ad430.api.WeeklyForecast

class WeeklyForecastFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val fab: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        fab.setOnClickListener {
            showLocationEntry()
        }

        // Prepare the Recyclerview
        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter =
            DailyForecastAdapter(tempDisplaySettingsManager) { forecastItem ->
                // launch the forecast details screen
                showForecastDetails(forecastItem)
            }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<WeeklyForecast> { weeklyForecast ->
            progressBar.visibility = View.GONE
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        }
        // attach the observer to the liveData
        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)


        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRepository.loadWeeklyForecast(savedLocation.zipcode)
                }

            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)


        return view
    }

    private fun showLocationEntry() {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()

        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val temp = forecast.temp.max
        val description = forecast.weather[0].description

        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
                temp,
                description
            )

        findNavController().navigate(action)
    }

}
