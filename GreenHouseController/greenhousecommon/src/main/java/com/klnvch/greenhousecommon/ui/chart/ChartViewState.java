package com.klnvch.greenhousecommon.ui.chart;

import android.graphics.Color;
import android.util.Pair;

import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartViewState {

    private final List<ChartLine> data = new ArrayList<>();

    public ChartViewState(List<PhoneState> phoneStates, List<ModuleState> moduleStates) {
        List<Pair<Long, Float>> networkStrength = new ArrayList<>();
        List<Pair<Long, Float>> batteryLevel = new ArrayList<>();
        for (PhoneState phoneState : phoneStates) {
            long time = TimeUnit.MILLISECONDS.toMinutes(phoneState.getTime());
            batteryLevel.add(new Pair<>(time, (float) phoneState.getBatteryLevel()));
            networkStrength.add(new Pair<>(time, (float) -phoneState.getNetworkStrength()));
        }

        List<Pair<Long, Float>> lightLevel = new ArrayList<>();
        List<Pair<Long, Float>> humidity = new ArrayList<>();
        List<Pair<Long, Float>> temperature = new ArrayList<>();
        List<Pair<Long, Float>> ws1S = new ArrayList<>();
        List<Pair<Long, Float>> ws2S = new ArrayList<>();
        List<Pair<Long, Float>> ws3S = new ArrayList<>();
        List<Pair<Long, Float>> ws1N = new ArrayList<>();
        List<Pair<Long, Float>> ws2N = new ArrayList<>();
        List<Pair<Long, Float>> ws3N = new ArrayList<>();
        for (ModuleState moduleState : moduleStates) {
            long time = TimeUnit.MILLISECONDS.toMinutes(moduleState.getTime());
            temperature.add(new Pair<>(time, moduleState.getTemperature()));
            humidity.add(new Pair<>(time, moduleState.getHumidity()));
            lightLevel.add(new Pair<>(time, moduleState.getLightLevel() / 10.0f));
            ws1S.add(new Pair<>(time, moduleState.getWs1S()/ 10.0f));
            ws2S.add(new Pair<>(time, moduleState.getWs2S()/ 10.0f));
            ws3S.add(new Pair<>(time, moduleState.getWs3S()/ 10.0f));
            ws1N.add(new Pair<>(time, moduleState.getWs1N()/ 10.0f));
            ws2N.add(new Pair<>(time, moduleState.getWs2N()/ 10.0f));
            ws3N.add(new Pair<>(time, moduleState.getWs3N()/ 10.0f));
        }

        data.add(new ChartLine(ws1S, "1S", Color.rgb(0,0,0)));
        data.add(new ChartLine(ws2S, "2S", Color.rgb(0,0,0)));
        data.add(new ChartLine(ws3S, "3S", Color.rgb(0,0,0)));
        data.add(new ChartLine(ws1N, "1N", Color.rgb(0,0,0)));
        data.add(new ChartLine(ws2N, "2N", Color.rgb(0,0,0)));
        data.add(new ChartLine(ws3N, "3N", Color.rgb(0,0,0)));

        data.add(new ChartLine(batteryLevel, "Battery level", Color.RED));
        data.add(new ChartLine(networkStrength, "Network strength", Color.BLUE));
        data.add(new ChartLine(temperature, "Temperature", Color.GREEN));
        data.add(new ChartLine(humidity, "Humidity", Color.GRAY));
        data.add(new ChartLine(lightLevel, "Light", Color.MAGENTA));
    }

    public List<ChartLine> getData() {
        return data;
    }
}
