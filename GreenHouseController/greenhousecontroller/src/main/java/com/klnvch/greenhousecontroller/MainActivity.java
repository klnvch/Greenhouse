package com.klnvch.greenhousecontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.klnvch.greenhousecommon.ui.action.ActionActivity;
import com.klnvch.greenhousecommon.ui.settings.DeviceIdDialog;
import com.klnvch.greenhousecommon.ui.states.StateActivity;
import com.klnvch.greenhousecontroller.databinding.ActivityMainBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HasSupportFragmentInjector {
    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, MainService.class));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.buttonState.setOnClickListener(
                v -> startActivity(new Intent(this, StateActivity.class)));
        binding.buttonActions.setOnClickListener(
                v -> startActivity(new Intent(this, ActionActivity.class)));
        binding.buttonExit.setOnClickListener(this);
        binding.buttonInfo.setOnClickListener(this);
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
            case R.id.buttonInfo:
                startActivity(new Intent(this, InfoActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_device_id) {
            new DeviceIdDialog().show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
