package com.klnvch.greenhousecommon.ui.states

import android.os.Bundle
import android.view.View
import com.klnvch.greenhousecommon.R
import com.klnvch.greenhousecommon.models.ModuleState
import com.klnvch.greenhousecommon.ui.states.listeners.ModuleStateInterface

class ModuleOutsideWeatherFragment : ItemStateFragment(),
    ModuleStateInterface {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImage(R.drawable.ic_baseline_wb_sunny_24)
    }

    override fun update(states: List<ModuleState>) {
        if (states.isEmpty()) {
            setDataMissingMessage()
            setAlert()
        } else {
            val latest = states.first()
            val temp = latest.temperature
            val minTemp = states.minOf { it.temperature }
            val maxTemp = states.maxOf { it.temperature }
            val humidity = latest.humidity
            val minHumidity = states.minOf { it.humidity }
            val maxHumidity = states.maxOf { it.humidity }
            val lightLevel = latest.lightLevel
            val minLightLevel = states.minOf { it.lightLevel }
            val maxLightLevel = states.maxOf { it.lightLevel }

            val msg = "$temp°C (Min: $minTemp°C, Max: $maxTemp°C)\n" +
                    "$humidity% (Min: $minHumidity%, Max: $maxHumidity%)\n" +
                    "$lightLevel (Min: $minLightLevel, Max: $maxLightLevel)"

            setMessage(msg)
            setNormal()
        }
    }
}
