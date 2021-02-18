package com.klnvch.greenhousecommon.ui.chart;

import android.util.Pair;

import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartViewState {
    private final List<Pair<Long, Integer>> batteryLevel = new ArrayList<>();
    private final List<Pair<Long, Integer>> networkStrength = new ArrayList<>();

    public ChartViewState(List<PhoneState> phoneStates) {
        for (PhoneState phoneState : phoneStates) {
            long time = TimeUnit.MILLISECONDS.toMinutes(phoneState.getTime());
            batteryLevel.add(new Pair<>(time, phoneState.getBatteryLevel()));
            networkStrength.add(new Pair<>(time, -phoneState.getNetworkStrength()));
        }
    }

    public List<Pair<Long, Integer>> getBatteryLevel() {
        return batteryLevel;
    }

    public List<Pair<Long, Integer>> getNetworkStrength() {
        return networkStrength;
    }
}
