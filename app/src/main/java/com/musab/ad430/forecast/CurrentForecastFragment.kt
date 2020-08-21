package com.musab.ad430.forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.musab.ad430.*

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


        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

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
        // create the observer
        val currentForecastObserver = Observer<DailyForecast> { forecastitem ->
            // update our list adapter
            dailyForecastAdapter.submitList(listOf(forecastitem))
            Log.d("TAG", "Data loaded from the repo")
        }

        forecastRepository.currentForecast.observe(viewLifecycleOwner, currentForecastObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    Log.d("TAG", "Saved location observer called current")
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

    private fun showForecastDetails(forecast: DailyForecast) {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment(
                forecast.temp,
                forecast.description
            )
        findNavController().navigate(action)
    }

    companion object {
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String): CurrentForecastFragment {
            val fragment = CurrentForecastFragment()

            val args = Bundle()

            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }


}
