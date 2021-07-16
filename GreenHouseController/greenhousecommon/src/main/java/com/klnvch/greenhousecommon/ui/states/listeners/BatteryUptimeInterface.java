package com.klnvch.greenhousecommon.ui.states.listeners;

import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public interface BatteryUptimeInterface {
    void onBatteryUptimeChanged(List<PhoneState> batteryUptime);
}
