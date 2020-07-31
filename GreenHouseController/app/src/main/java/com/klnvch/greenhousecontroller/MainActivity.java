package com.klnvch.greenhousecontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_DEVICE_ADDRESS = "KEY_DEVICE_ADDRESS";
    private static final String SHARED_PREFERENCES_NAME = "settings";

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String deviceAddress = sharedPreferences.getString(KEY_DEVICE_ADDRESS, "98:D3:33:F5:A3:24");
        MainService.start(this, deviceAddress);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.deviceAddressInput.setText(deviceAddress);
        binding.buttonExit.setOnClickListener(this);
        binding.deviceAddressSubmitButton.setOnClickListener(this);
        binding.commandGetData.setOnClickListener(this);
        binding.commandSetTime.setOnClickListener(this);
        binding.commandGetGlobalLimits.setOnClickListener(this);
        binding.commandSetGlobalLimits.setOnClickListener(this);
        binding.commandStartVentilation.setOnClickListener(this);
        binding.commandSector0.setOnClickListener(this);
        binding.commandSector1.setOnClickListener(this);
        binding.commandSector2.setOnClickListener(this);
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
            case R.id.device_address_submit_button:
                String deviceAddress = binding.deviceAddressInput.getText().toString().trim();
                sharedPreferences.edit().putString(KEY_DEVICE_ADDRESS, deviceAddress).apply();
                MainService.start(this, deviceAddress);
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
        }
    }
}
