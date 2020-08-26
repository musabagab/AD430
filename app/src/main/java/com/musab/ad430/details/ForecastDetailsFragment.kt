package com.musab.ad430.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.musab.ad430.TempDisplaySettingsManager
import com.musab.ad430.databinding.FragmentForecastDetailsBinding
import com.musab.ad430.formatTempForDisplay

class ForecastDetailsFragment : Fragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()

    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager
    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )
    private var _binding: FragmentForecastDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingsManager = TempDisplaySettingsManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> { viewState ->
            // update the UI
            binding.tempText.text = formatTempForDisplay(
                viewState.temp,
                tempDisplaySettingsManager.getTempDisplaySetting()
            )
            binding.descriptionText.text = viewState.description
        }

        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}