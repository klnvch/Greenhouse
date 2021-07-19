package com.klnvch.greenhousecommon.ui.states

import android.os.Bundle
import android.view.View
import com.klnvch.greenhousecommon.R
import com.klnvch.greenhousecommon.models.PhoneState
import com.klnvch.greenhousecommon.ui.states.listeners.PhoneStateInterface

class StateTimeAndDeviceIdFragment : ItemStateFragment(), PhoneStateInterface {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImage(R.drawable.ic_baseline_router_24)
    }

    override fun update(states: List<PhoneState>) {
        if (states.isEmpty()) {
            setDataMissingMessage()
            setAlert()
        } else {
            val latest = states.first()
            val deviceId = latest.deviceId
            val time = latest.time
            val currentTime = System.currentTimeMillis()
            val timeStr = formatTime(time)

            setMessage("$timeStr\t($deviceId)")

            if (states.size > 1) {
                val timeDelta = (time - states[1].time) / 60000L
                setMessage2("Time delta: $timeDelta min.")
            }

            if (currentTime - time > ALERT_TIME_DIFFERENCE) {
                setAlert()
            } else {
                setNormal()
            }
        }
    }

    override fun getDescription(): String {
        return "Last time phone data is updated with device id."
    }
}
