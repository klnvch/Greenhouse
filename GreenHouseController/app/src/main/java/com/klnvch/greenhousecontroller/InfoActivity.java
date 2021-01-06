package com.klnvch.greenhousecontroller;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.bluetooth.BluetoothRestartCounter;
import com.klnvch.greenhousecontroller.databinding.ActivityInfoBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public final class InfoActivity extends AppCompatActivity {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        binding.timeValue.setText(sdf.format(new Date()));

        PhoneStatusManager phoneStatusManager = PhoneStatusManager.init(this);
        int signalStrength = phoneStatusManager.getCellularNetworkStrength();
        binding.signalStrengthValue.setText(Integer.toString(signalStrength));
        boolean isCharging = phoneStatusManager.isBatteryIsCharging();
        binding.batteryChargingValue.setText(Boolean.toString(isCharging));
        int batteryLevel = phoneStatusManager.getBatteryLevel();
        binding.batteryLevelValue.setText(Integer.toString(batteryLevel));

        compositeDisposable.add(BluetoothRestartCounter.getInstance().getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> binding.restartBluetoothCounterValue.setText(Long.toString(i))));

        binding.restartNow.setOnClickListener(v -> BluetoothRestartCounter.getInstance().completeNow());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
