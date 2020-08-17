package com.klnvch.greenhousecontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = AppSettings.getInstance(this);
        String deviceAddress = settings.getDeviceAddress();
        String deviceId = settings.getDeviceId();
        MainService.start(this, deviceAddress, deviceId);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.deviceAddressInput.setText(deviceAddress);
        binding.deviceIdInput.setText(deviceId);
        binding.buttonExit.setOnClickListener(this);
        binding.buttonLogs.setOnClickListener(this);
        binding.buttonInfo.setOnClickListener(this);
        binding.deviceAddressSubmitButton.setOnClickListener(this);
        binding.commandGetData.setOnClickListener(this);
        binding.commandSetTime.setOnClickListener(this);
        binding.commandGetGlobalLimits.setOnClickListener(this);
        binding.commandSetGlobalLimits.setOnClickListener(this);
        binding.commandStartVentilation.setOnClickListener(this);
        binding.commandSector0.setOnClickListener(this);
        binding.commandSector1.setOnClickListener(this);
        binding.commandSector2.setOnClickListener(this);
        binding.commandCustom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonExit:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.exit_confirmation_msg)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            stopService(new Intent(this, MainService.class));
                            finish();
                        })
                        .show();
                break;
            case R.id.buttonLogs:
                startActivity(new Intent(this, LogsActivity.class));
                break;
            case R.id.buttonInfo:
                startActivity(new Intent(this, InfoActivity.class));
                break;
            case R.id.device_address_submit_button:
                String deviceAddress = binding.deviceAddressInput.getText().toString().trim();
                String deviceId = binding.deviceIdInput.getText().toString().trim();
                settings.setDeviceAddress(deviceAddress);
                MainService.start(this, deviceAddress, deviceId);
                break;
            case R.id.command_get_data:
                Command.getData();
                break;
            case R.id.command_set_time:
                Command.setTime();
                break;
            case R.id.command_get_global_limits:
                Command.getGlobalLimits();
                break;
            case R.id.command_set_global_limits:
                Command.setGlobalLimits();
                break;
            case R.id.command_start_ventilation:
                Command.startVentilation();
                break;
            case R.id.command_sector0:
                SectorActivity.startActivity(this, 0);
                break;
            case R.id.command_sector1:
                SectorActivity.startActivity(this, 1);
                break;
            case R.id.command_sector2:
                SectorActivity.startActivity(this, 2);
                break;
            case R.id.command_custom:
                String command = binding.customCommandValue.getText().toString().trim();
                Command.sendCommand(command);
                break;
        }
    }
}
