package com.klnvch.greenhousecommon.ui.states

import com.klnvch.greenhousecommon.R
import com.klnvch.greenhousecommon.models.PhoneState
import com.klnvch.greenhousecommon.ui.states.listeners.BatteryUptimeInterface
import com.klnvch.greenhousecommon.ui.states.listeners.PhoneStateInterface

class BatteryStateFragment : ItemStateFragment(),
    PhoneStateInterface, BatteryUptimeInterface {
    override fun update(states: List<PhoneState>) {
        if (states.isEmpty()) {
            setImage(R.drawable.ic_baseline_battery_alert_24)
            setDataMissingMessage()
            setAlert()
        } else {
            val latest = states.first()
            val isCharging = latest.isCharging
            val batteryLevel = latest.batteryLevel
            when {
                isCharging -> {
                    setImage(R.drawable.ic_baseline_battery_charging_full_24)
                }
                batteryLevel > 50 -> {
                    setImage(R.drawable.ic_baseline_battery_full_24)
                }
                else -> {
                    setImage(R.drawable.ic_baseline_battery_alert_24)
                }
            }
            val isChargingStr =
                if (isCharging) getString(R.string.state_message_battery_charging)
                else getString(R.string.state_message_battery_discharging)
            val msg = "$batteryLevel% $isChargingStr"
            setMessage(msg)
            setNormal()
        }
    }

    override fun onBatteryUptimeChanged(batteryUptime: MutableList<PhoneState>) {
        if (batteryUptime.isNotEmpty()) {
            setMessage2("Uptime: " + formatTime(batteryUptime.first().time))
        }
    }
}
