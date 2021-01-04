package com.klnvch.greenhousecontroller.ui;

import com.klnvch.greenhousecontroller.R;
import com.klnvch.greenhousecontroller.models.PhoneData;
import com.klnvch.greenhousecontroller.ui.states.ItemStateFragment;

import java.util.List;

public class BatteryStateFragment extends ItemStateFragment {

    public void update(List<PhoneData> phoneData) {
        if (phoneData == null || phoneData.size() == 0) {
            binding.image.setImageResource(R.drawable.ic_baseline_battery_alert_24);
            binding.message.setText(null);
        } else {
            PhoneData latest = phoneData.get(0);
            boolean isCharging = latest.isCharging();
            int batteryLevel = latest.getBatteryLevelInt();
            if (isCharging) {
                binding.image.setImageResource(R.drawable.ic_baseline_battery_charging_full_24);
            } else if (batteryLevel > 50) {
                binding.image.setImageResource(R.drawable.ic_baseline_battery_full_24);
            } else {
                binding.image.setImageResource(R.drawable.ic_baseline_battery_alert_24);
            }
            String msg = batteryLevel + "%";
            binding.message.setText(msg);
        }
    }
}
