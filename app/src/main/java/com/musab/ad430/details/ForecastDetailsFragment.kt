package com.musab.ad430.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.musab.ad430.R
import com.musab.ad430.TempDisplaySettingsManager
import com.musab.ad430.formatTempForDisplay

class ForecastDetailsFragment : Fragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()

    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast_details, container, false)

        tempDisplaySettingsManager = TempDisplaySettingsManager(requireContext())

        val tempText = view.findViewById<TextView>(R.id.tempText);
        val descriptionText = view.findViewById<TextView>(R.id.descriptionText);

        tempText.text = formatTempForDisplay(
            args.temp,
            tempDisplaySettingsManager.getTempDisplaySetting()
        )
        descriptionText.text = args.description
        return view
    }


}