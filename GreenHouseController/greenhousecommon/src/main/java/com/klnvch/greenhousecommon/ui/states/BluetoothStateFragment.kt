package com.klnvch.greenhousecommon.ui.states

import android.os.Bundle
import android.view.View
import com.klnvch.greenhousecommon.R
import com.klnvch.greenhousecommon.models.BluetoothState
import com.klnvch.greenhousecommon.models.PhoneState
import com.klnvch.greenhousecommon.ui.states.listeners.BluetoothUptimeInterface
import com.klnvch.greenhousecommon.ui.states.listeners.PhoneStateInterface

class BluetoothStateFragment : ItemStateFragment(),
    PhoneStateInterface, BluetoothUptimeInterface {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearMessage()
    }

    override fun update(states: List<PhoneState>) {
        if (states.isEmpty()) {
            setImage(R.drawable.ic_baseline_bluetooth_disabled_24)
            setDataMissingMessage()
            setAlert()
        } else {
            val latest = states.first()
            val bluetoothState = latest.bluetoothState
            val error = latest.bluetoothError
            when (bluetoothState) {
                BluetoothState.CONNECTED -> {
                    setImage(R.drawable.ic_baseline_bluetooth_connected_24)
                    setMessage(R.string.state_message_bluetooth_connected)
                    setNormal()
                }
                BluetoothState.NOT_AVAILABLE -> {
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24)
                    setMessage(R.string.state_message_bluetooth_not_available)
                    setAlert()
                }
                BluetoothState.NOT_ENABLED -> {
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24)
                    setMessage(R.string.state_message_bluetooth_not_enabled)
                    setAlert()
                }
                BluetoothState.ADDRESS_NOT_VALID -> {
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24)
                    setMessage(error)
                    setAlert()
                }
                BluetoothState.SOCKET_CONNECT_ERROR -> {
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24)
                    setMessage(R.string.state_message_socket_connect_error)
                    setAlert()
                }
                BluetoothState.READ_ERROR, BluetoothState.WRITE_ERROR, BluetoothState.SOCKET_ERROR -> {
                    setImage(R.drawable.ic_baseline_bluetooth_searching_24)
                    setMessage(error)
                    setAlert()
                }
                else -> {
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24)
                    setMessage(R.string.state_message_bluetooth_unknown_error)
                    setAlert()
                }
            }
        }
    }

    override fun onBluetoothUptimeUpdated(bluetoothUptime: MutableList<PhoneState>) {
        if (bluetoothUptime.isNotEmpty()) {
            setMessage2("Uptime: " + formatTime(bluetoothUptime.first().time))
        }
    }
}
