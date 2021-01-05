package com.klnvch.greenhousecontroller.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecontroller.R;
import com.klnvch.greenhousecontroller.models.PhoneState;

import java.util.List;

public class SignalStateFragment extends ItemStateFragment implements PhoneStateInterface {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImage(R.drawable.ic_baseline_network_cell_24);
    }

    @Override
    public void update(List<PhoneState> states) {
        if (states == null || states.size() == 0) {
            setDataMissingMessage();
            setAlert();
        } else {
            PhoneState latest = states.get(0);
            int signalStrength = latest.getNetworkStrength();
            String msg = signalStrength + " db";
            setMessage(msg);
            setNormal();
        }
    }
}
