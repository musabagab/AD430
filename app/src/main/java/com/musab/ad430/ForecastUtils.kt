package com.musab.ad430

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatTempForDisplay(temp: Float, tempDisplaySetting: TempDisplaySetting): String {

    return when (tempDisplaySetting) {

        TempDisplaySetting.Fahrenheit -> String.format("%.2f F", temp)
        TempDisplaySetting.Celsius -> {
            val temp = (temp - 32) * (5f / 9f)
            String.format("%.2f C", temp)
        }
    }
}

fun showTempDisplaySettingsDialog(
    context: Context,
    tempDisplaySettingsManager: TempDisplaySettingsManager
) {
    val dialogBuilder = AlertDialog.Builder(context)
        .setTitle("Choose Display Units")
        .setMessage("choose which temperature units to use for temperature display")
        .setPositiveButton("F") { _, _ ->
            tempDisplaySettingsManager.updateSettings(TempDisplaySetting.Fahrenheit)
        }.setNeutralButton("C") { _, _ ->
            tempDisplaySettingsManager.updateSettings(TempDisplaySetting.Celsius)
        }.setOnDismissListener {
            Toast.makeText(context, "Settings will take affect on app restart", Toast.LENGTH_SHORT)
                .show()
        }

    dialogBuilder.show()
}