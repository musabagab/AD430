package com.musab.ad430

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.musab.ad430.api.DailyForecast
import java.text.SimpleDateFormat
import java.util.*

private val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyyy")

class DailyForecastViewHolder(
    view: View,
    private val tempDisplaySettingsManager: TempDisplaySettingsManager
) : RecyclerView.ViewHolder(view) {
    private val tempText = view.findViewById<TextView>(R.id.tempText)
    private val descriptionText = view.findViewById<TextView>(R.id.descriptionText)
    private val dateText = view.findViewById<TextView>(R.id.dateText)
    private val forecastIcon = view.findViewById<ImageView>(R.id.forecastIcon)

    fun bind(dailyForecast: DailyForecast) {
        tempText.text = formatTempForDisplay(
            dailyForecast.temp.max,
            tempDisplaySettingsManager.getTempDisplaySetting()
        )
        descriptionText.text = dailyForecast.weather[0].description
        dateText.text = DATE_FORMAT.format(Date(dailyForecast.date * 1000))

        val iconId = dailyForecast.weather[0].icon
        forecastIcon.load("http://openweathermap.org/img/wn/${iconId}@2x.png")
    }
}

class DailyForecastAdapter(
    private val tempDisplaySettingsManager: TempDisplaySettingsManager,
    private val clickHandler: (DailyForecast) -> Unit

) : ListAdapter<DailyForecast, DailyForecastViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(itemView, tempDisplaySettingsManager)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, postion: Int) {
        holder.bind(getItem(postion))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(postion))
        }
    }

}