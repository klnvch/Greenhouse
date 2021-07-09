package com.klnvch.greenhousecommon.ui.states

import android.os.Bundle
import android.view.View
import com.klnvch.greenhousecommon.R
import com.klnvch.greenhousecommon.models.ModuleState

open class WaterSensorStateFragment : ItemStateFragment(), ModuleStateInterface {

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

            setMessage(
                "1N: " + latest.ws1N + ", 2N: " + latest.ws2N + ", 3N: " + latest.ws3N + "\n" +
                        "1S: " + latest.ws1S + ", 2S: " + latest.ws2S + ", 3S: " + latest.ws3S
            )
            setNormal()
        }
    }
}
