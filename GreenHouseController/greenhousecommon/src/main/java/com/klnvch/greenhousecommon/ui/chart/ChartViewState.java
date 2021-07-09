package com.klnvch.greenhousecommon.ui.chart;

import android.util.Pair;

import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartViewState {
    private final List<Pair<Long, Float>> batteryLevel = new ArrayList<>();
    private final List<Pair<Long, Float>> networkStrength = new ArrayList<>();
    private final List<Pair<Long, Float>> temperature = new ArrayList<>();
    private final List<Pair<Long, Float>> humidity = new ArrayList<>();
    private final List<Pair<Long, Float>> lightLevel = new ArrayList<>();

    public ChartViewState(List<PhoneState> phoneStates, List<ModuleState> moduleStates) {
        for (PhoneState phoneState : phoneStates) {
            long time = TimeUnit.MILLISECONDS.toMinutes(phoneState.getTime());
            batteryLevel.add(new Pair<>(time, (float) phoneState.getBatteryLevel()));
            networkStrength.add(new Pair<>(time, (float) -phoneState.getNetworkStrength()));
        }

        for (ModuleState moduleState : moduleStates) {
            long time = TimeUnit.MILLISECONDS.toMinutes(moduleState.getTime());
            temperature.add(new Pair<>(time, moduleState.getTemperature()));
            humidity.add(new Pair<>(time, moduleState.getHumidity()));
            lightLevel.add(new Pair<>(time, moduleState.getLightLevel() / 10.0f));
        }
    }

    public List<Pair<Long, Float>> getBatteryLevel() {
        return batteryLevel;
    }

    public List<Pair<Long, Float>> getNetworkStrength() {
        return networkStrength;
    }

    public List<Pair<Long, Float>> getTemperature() {
        return temperature;
    }

    public List<Pair<Long, Float>> getHumidity() {
        return humidity;
    }

    public List<Pair<Long, Float>> getLightLevel() {
        return lightLevel;
    }
}
