package com.musab.ad430.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.musab.ad430.Location
import com.musab.ad430.LocationRepository
import com.musab.ad430.R


class LocationEntryFragment : Fragment() {
    private lateinit var locationRepository: LocationRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        locationRepository = LocationRepository(requireContext())
        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)


        val zipCodeEditText: EditText = view.findViewById(R.id.zipcode_edittext)
        val submitButton: Button = view.findViewById(R.id.submit_btn)


        submitButton.setOnClickListener {
            val zipcode = zipCodeEditText.text.toString()

            if (zipcode.length != 5) {
                Toast.makeText(requireContext(), R.string.validzipcode, Toast.LENGTH_LONG).show()
            } else {
                locationRepository.saveLocation(Location.Zipcode(zipcode))
                findNavController().navigateUp()
            }

        }


        return view
    }


}
