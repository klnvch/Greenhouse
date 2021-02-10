package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;


import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StateTimeAndDeviceIdFragment extends ItemStateFragment implements PhoneStateInterface {
    private static final String TIME_PATTERN = "dd/mm HH:mm";
    private SimpleDateFormat sdf = null;
    private static final long ALERT_TIME_DIFFERENCE = 30 * 60 * 1000;  // 30 minutes

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImage(R.drawable.ic_baseline_router_24);
        sdf = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
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

            String timeStr = sdf.format(new Date(time));
            String msg = timeStr + "\t(" + deviceId + ")";
            setMessage(msg);

            if (currentTime - time > ALERT_TIME_DIFFERENCE) {
                setAlert();
            } else {
                setNormal();
            }
        }
    }
}
