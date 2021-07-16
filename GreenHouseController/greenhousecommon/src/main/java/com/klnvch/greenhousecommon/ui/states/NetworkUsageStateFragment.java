package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.PhoneState;
import com.klnvch.greenhousecommon.ui.states.listeners.PhoneStateInterface;

import java.util.List;

public class NetworkUsageStateFragment extends ItemStateFragment implements PhoneStateInterface {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImage(R.drawable.ic_baseline_data_usage_24);
    }

    @Override
    public void update(List<PhoneState> states) {
        if (states == null || states.size() == 0) {
            setDataMissingMessage();
            setAlert();
        } else {
            PhoneState latest = states.get(0);

            long rxDeviceMobile = latest.getRxDeviceMobile() / 1024;
            long txDeviceMobile = latest.getTxDeviceMobile() / 1024;
            long rxDeviceWiFi = latest.getRxDeviceWifi() / 1024;
            long txDeviceWiFi = latest.getTxDeviceWifi() / 1024;
            long rxAppMobile = latest.getRxAppMobile() / 1024;
            long txAppMobile = latest.getTxAppMobile() / 1024;
            long rxAppWiFi = latest.getRxAppWifi() / 1024;
            long txAppWiFi = latest.getTxAppWifi() / 1024;

            setMessage("Device mobile: " + rxDeviceMobile + "/" + txDeviceMobile + " KB\n"
                    + "Device Wi-fi: " + rxDeviceWiFi + "/" + txDeviceWiFi + " KB\n"
                    + "App mobile: " + rxAppMobile + "/" + txAppMobile + " KB\n"
                    + "App Wi-fi: " + rxAppWiFi + "/" + txAppWiFi + " KB");
            setNormal();
        }
    }
}
