package com.musab.ad430.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.musab.ad430.AppNavigator
import com.musab.ad430.R


/**
 * A simple [Fragment] subclass.
 */
class LocationEntryFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)


        val zipCodeEditText: EditText = view.findViewById(R.id.zipcode_edittext)
        val submitButton: Button = view.findViewById(R.id.submit_btn)


        submitButton.setOnClickListener {
            val zipcode = zipCodeEditText.text.toString()

            if (zipcode.length != 5) {
                Toast.makeText(requireContext(), R.string.validzipcode, Toast.LENGTH_LONG).show()
            } else {
                appNavigator.navigateToCurrentForecast(zipcode)
            }

        }


        return view
    }


}
