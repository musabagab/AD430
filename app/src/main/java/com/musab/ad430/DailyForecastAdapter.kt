package com.musab.ad430

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DailyForecastViewHolder(
    view: View,
    private val tempDisplaySettingsManager: TempDisplaySettingsManager
) : RecyclerView.ViewHolder(view) {
    private val tempText = view.findViewById<TextView>(R.id.tempText)
    private val descriptionText = view.findViewById<TextView>(R.id.descriptionText)


    fun bind(dailyForecast: DailyForecast) {
        tempText.text = formatTempForDisplay(
            dailyForecast.temp,
            tempDisplaySettingsManager.getTempDisplaySetting()
        )
        descriptionText.text = dailyForecast.description
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