package com.klnvch.greenhousecommon.ui.states.listeners;

import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public interface BluetoothUptimeInterface {
    void onBluetoothUptimeUpdated(List<PhoneState> bluetoothUptime);
}
