package com.musab.ad430.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.musab.ad430.*

class WeeklyForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager
    private val forecastRepository = ForecastRepository()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingsManager = TempDisplaySettingsManager(requireContext())


        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

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

        val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
            Toast.makeText(requireContext(), "Loaded items", Toast.LENGTH_SHORT).show()
        }
        // attach the observer to the liveData
        forecastRepository.weeklyForecast.observe(requireActivity(), weeklyForecastObserver)
        // load the data
        forecastRepository.loadForecast(zipcode)

        return view
    }

    private fun showLocationEntry() {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()

        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
                forecast.temp,
                forecast.description
            )

        findNavController().navigate(action)
    }

    companion object {
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String): WeeklyForecastFragment {
            val fragment = WeeklyForecastFragment()

            val args = Bundle()

            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }


}
