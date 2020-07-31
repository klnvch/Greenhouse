package com.klnvch.greenhousecontroller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.databinding.ActivityInfoBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss", Locale.getDefault());
        binding.timeValue.setText(sdf.format(new Date()));

        PhoneStatusManager phoneStatusManager = PhoneStatusManager.init(this);
        Integer signalStrength = phoneStatusManager.getCellularNetworkStrength();
        binding.signalStrengthValue.setText(signalStrength != null ? Integer.toString(signalStrength) : null);
        Boolean isCharging = phoneStatusManager.isBatteryIsCharging();
        binding.batteryChargingValue.setText(isCharging != null ? Boolean.toString(isCharging) : null);
        Integer batteryLevel = phoneStatusManager.getBatteryLevel();
        binding.batteryLevelValue.setText(batteryLevel != null ? Integer.toString(batteryLevel) : null);
    }
}
