package com.klnvch.greenhousecommon.ui.states;

import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public class ViewState {
    private final List<PhoneState> phoneStates;
    private final List<ModuleState> moduleStates;
    private final List<PhoneState> batteryUptime;
    private final List<PhoneState> bluetoothUptime;

    public ViewState(List<PhoneState> phoneStates,
                     List<ModuleState> moduleStates,
                     List<PhoneState> batteryUptime,
                     List<PhoneState> bluetoothUptime) {
        this.phoneStates = phoneStates;
        this.moduleStates = moduleStates;
        this.batteryUptime = batteryUptime;
        this.bluetoothUptime = bluetoothUptime;
    }

    public List<ModuleState> getModuleStates() {
        return moduleStates;
    }

    public List<PhoneState> getPhoneStates() {
        return phoneStates;
    }

    public List<PhoneState> getBatteryUptime() {
        return batteryUptime;
    }

    public List<PhoneState> getBluetoothUptime() {
        return bluetoothUptime;
    }
}
