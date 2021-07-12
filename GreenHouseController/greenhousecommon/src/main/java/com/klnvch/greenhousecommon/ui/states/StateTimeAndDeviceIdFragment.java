package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public class StateTimeAndDeviceIdFragment extends ItemStateFragment implements PhoneStateInterface {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImage(R.drawable.ic_baseline_router_24);
    }

    @Override
    public void update(List<PhoneState> states) {
        if (states == null || states.size() == 0) {
            setDataMissingMessage();
            setAlert();
        } else {
            PhoneState latest = states.get(0);
            String deviceId = latest.getDeviceId();
            long time = latest.getTime();
            long currentTime = System.currentTimeMillis();

            String timeStr = formatTime(time);
            String msg = timeStr + "\t(" + deviceId + ")";
            setMessage(msg);

            if (currentTime - time > ALERT_TIME_DIFFERENCE) {
                setAlert();
            } else {
                setNormal();
            }
        }
    }

    @Override
    protected String getDescription() {
        return "Last time phone data is updated with device id.";
    }
}
