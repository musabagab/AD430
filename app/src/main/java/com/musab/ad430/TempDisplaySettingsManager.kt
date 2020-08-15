package com.musab.ad430

import android.content.Context

enum class TempDisplaySetting {
    Fahrenheit, Celsius
}

class TempDisplaySettingsManager(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun updateSettings(setting: TempDisplaySetting) =
        preferences.edit().putString("key_temp_display", setting.name).commit()

    fun getTempDisplaySetting(): TempDisplaySetting {
        val settingValue =
            preferences.getString("key_temp_display", TempDisplaySetting.Fahrenheit.name)
                ?: TempDisplaySetting.Fahrenheit.name

        return TempDisplaySetting.valueOf(settingValue)
    }


}