package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.ModuleState;

import java.util.List;

public class ModuleOutsideWeatherFragment extends ItemStateFragment implements ModuleStateInterface {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImage(R.drawable.ic_baseline_wb_sunny_24);
    }

    @Override
    public void update(List<ModuleState> states) {
        if (states == null || states.size() == 0) {
            setDataMissingMessage();
            setAlert();
        } else {
            ModuleState latest = states.get(0);
            float temperature = latest.getTemperature();
            float humidity = latest.getHumidity();
            int lightLevel = latest.getLightLevel();
            String msg = temperature + "°C " + humidity + "% " + lightLevel;
            setMessage(msg);
            setNormal();
        }
    }
}
