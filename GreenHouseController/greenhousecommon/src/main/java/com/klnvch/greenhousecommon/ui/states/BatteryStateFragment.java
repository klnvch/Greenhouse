package com.klnvch.greenhousecommon.ui.states;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public class BatteryStateFragment extends ItemStateFragment implements PhoneStateInterface {

    @Override
    public void update(List<PhoneState> states) {
        if (states == null || states.size() == 0) {
            setImage(R.drawable.ic_baseline_battery_alert_24);
            setDataMissingMessage();
            setAlert();
        } else {
            PhoneState latest = states.get(0);
            boolean isCharging = latest.isCharging();
            int batteryLevel = latest.getBatteryLevel();
            if (isCharging) {
                setImage(R.drawable.ic_baseline_battery_charging_full_24);
            } else if (batteryLevel > 50) {
                setImage(R.drawable.ic_baseline_battery_full_24);
            } else {
                setImage(R.drawable.ic_baseline_battery_alert_24);
            }
            String msg = batteryLevel + "%";
            setMessage(msg);
            setNormal();
        }
    }
}
