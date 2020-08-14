package com.musab.ad430

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val forecastRepository = ForecastRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zipCodeEditText: EditText = findViewById(R.id.zipcode_edittext)

        val submitButton: Button = findViewById(R.id.submit_btn)

        submitButton.setOnClickListener {
            val zipcode = zipCodeEditText.text.toString()

            if (zipcode.length != 5) {
                Toast.makeText(this, R.string.validzipcode, Toast.LENGTH_LONG).show()
            } else {
                forecastRepository.loadForecast(zipcode)
            }

        }
        // Prepare the Recyclerview
        val forecastList: RecyclerView = findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(this)
        val dailyForecastAdapter = DailyForecastAdapter {
            val msg = getString(R.string.forecast_clicked_format, it.temp, it.description)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
            Toast.makeText(this, "Loaded items", Toast.LENGTH_SHORT).show()
        }
        // attach the observer to the liveData
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)
    }


}
