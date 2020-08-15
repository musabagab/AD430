package com.musab.ad430

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musab.ad430.details.ForecastDetailsActivity

class MainActivity : AppCompatActivity() {

    private val forecastRepository = ForecastRepository()
    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zipCodeEditText: EditText = findViewById(R.id.zipcode_edittext)
        val submitButton: Button = findViewById(R.id.submit_btn)

        tempDisplaySettingsManager = TempDisplaySettingsManager(this)


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
        val dailyForecastAdapter =
            DailyForecastAdapter(tempDisplaySettingsManager) { forecastItem ->
                // launch the forecast details screen
                showForecastDetails(forecastItem)
            }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
            Toast.makeText(this, "Loaded items", Toast.LENGTH_SHORT).show()
        }
        // attach the observer to the liveData
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val intent = Intent(this, ForecastDetailsActivity::class.java)
        intent.putExtra("key_temp", forecast.temp)
        intent.putExtra("key_description", forecast.description)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tempDisplaySettings -> {
                showTempDisplaySettingsDialog(this, tempDisplaySettingsManager)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
