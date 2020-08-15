package com.musab.ad430.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.musab.ad430.R
import com.musab.ad430.TempDisplaySettingsManager
import com.musab.ad430.formatTempForDisplay
import com.musab.ad430.showTempDisplaySettingsDialog

class ForecastDetailsActivity : AppCompatActivity() {

    private lateinit var tempDisplaySettingsManager: TempDisplaySettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_details)

        tempDisplaySettingsManager = TempDisplaySettingsManager(this)

        val tempText = findViewById<TextView>(R.id.tempText);
        val descriptionText = findViewById<TextView>(R.id.descriptionText);


        tempText.text = formatTempForDisplay(
            intent.getFloatExtra("key_temp", 0.0f),
            tempDisplaySettingsManager.getTempDisplaySetting()
        )

        descriptionText.text = intent.getStringExtra("key_description")

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