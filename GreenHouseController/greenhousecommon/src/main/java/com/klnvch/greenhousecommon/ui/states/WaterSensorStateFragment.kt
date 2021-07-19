package com.klnvch.greenhousecommon.ui.states

import android.os.Bundle
import android.view.View
import com.klnvch.greenhousecommon.R
import com.klnvch.greenhousecommon.models.ModuleState
import com.klnvch.greenhousecommon.ui.states.listeners.ModuleStateInterface

open class WaterSensorStateFragment : ItemStateFragment(),
    ModuleStateInterface {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImage(R.drawable.ic_baseline_grain_24)
    }

    override fun update(states: MutableList<ModuleState>?) {
        if (states.isNullOrEmpty()) {
            setDataMissingMessage()
            setAlert()
        } else {
            val latest: ModuleState = states.first()

            val ws1N = latest.ws1N
            val ws1NMin = states.minOf { it.ws1N }
            val ws1NMax = states.maxOf { it.ws1N }
            val ws1NAvg = states.map { it.ws1N }.average().toInt()

            val ws2N = latest.ws2N
            val ws2NMin = states.minOf { it.ws2N }
            val ws2NMax = states.maxOf { it.ws2N }
            val ws2NAvg = states.map { it.ws2N }.average().toInt()

            val ws3N = latest.ws3N
            val ws3NMin = states.minOf { it.ws3N }
            val ws3NMax = states.maxOf { it.ws3N }
            val ws3NAvg = states.map { it.ws3N }.average().toInt()

            val ws1S = latest.ws1S
            val ws1SMin = states.minOf { it.ws1S }
            val ws1SMax = states.maxOf { it.ws1S }
            val ws1SAvg = states.map { it.ws1S }.average().toInt()

            val ws2S = latest.ws2S
            val ws2SMin = states.minOf { it.ws2S }
            val ws2SMax = states.maxOf { it.ws2S }
            val ws2SAvg = states.map { it.ws2S }.average().toInt()

            val ws3S = latest.ws3S
            val ws3SMin = states.minOf { it.ws3S }
            val ws3SMax = states.maxOf { it.ws3S }
            val ws3SAvg = states.map { it.ws3S }.average().toInt()

            setMessage(
                "1N: $ws1N (Min: $ws1NMin, Max: $ws1NMax, Avg: $ws1NAvg)\n" +
                        "2N: $ws2N (Min: $ws2NMin, Max: $ws2NMax, Avg: $ws2NAvg)\n" +
                        "3N: $ws3N (Min: $ws3NMin, Max: $ws3NMax, Avg: $ws3NAvg)\n" +
                        "1S: $ws1S (Min: $ws1SMin, Max: $ws1SMax, Avg: $ws1SAvg)\n" +
                        "2S: $ws2S (Min: $ws2SMin, Max: $ws2SMax, Avg: $ws2SAvg)\n" +
                        "3S: $ws3S (Min: $ws3SMin, Max: $ws3SMax, Avg: $ws3SAvg)"
            )
            setNormal()
        }
    }
}
