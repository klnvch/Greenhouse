package com.klnvch.greenhousecontroller.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecontroller.R;
import com.klnvch.greenhousecontroller.models.PhoneState;

import java.util.List;

public class BluetoothStateFragment extends ItemStateFragment implements PhoneStateInterface {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMessage(null);
    }

    @Override
    public void update(List<PhoneState> states) {
        if (states == null || states.size() == 0) {
            setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
            setDataMissingMessage();
            setAlert();
        } else {
            PhoneState latest = states.get(0);
            boolean isBluetoothActive = latest.isBluetoothActive();
            if (isBluetoothActive) {
                setImage(R.drawable.ic_baseline_bluetooth_connected_24);
            } else {
                setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
            }
            setNormal();
        }
    }
}
