package com.klnvch.greenhousecontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.databinding.ActivitySectorBinding;

public class SectorActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_SECTOR_ID = "sector_id";

    static void startActivity(Context context, int sectorId) {
        context.startActivity(new Intent(context, SectorActivity.class)
                .putExtra(KEY_SECTOR_ID, sectorId));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.sector_id, getSectorId()));
        ActivitySectorBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sector);
        binding.buttonStartWatering.setOnClickListener(this);
        binding.buttonGetWateringDefaults.setOnClickListener(this);
        binding.buttonSetWateringDefaults.setOnClickListener(this);
        binding.buttonStartVentilation.setOnClickListener(this);
        binding.buttonCloseWindow.setOnClickListener(this);
        binding.buttonOpenWindow.setOnClickListener(this);
        binding.buttonSetClimateDefaults.setOnClickListener(this);
        binding.buttonGetWateringDefaults.setOnClickListener(this);
    }

    private int getSectorId() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getIntExtra(KEY_SECTOR_ID, 0);
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        int sectorId = getSectorId();
        switch (v.getId()) {
            case R.id.buttonStartWatering:
                Command.startWatering(sectorId);
                break;
            case R.id.buttonGetWateringDefaults:
                Command.getWateringDefaults(sectorId);
                break;
            case R.id.buttonSetWateringDefaults:
                Command.setWateringDefaults(sectorId);
                break;
            case R.id.buttonStartVentilation:
                Command.startVentilation(sectorId);
                break;
            case R.id.buttonCloseWindow:
                Command.closeWindow(sectorId);
                break;
            case R.id.buttonOpenWindow:
                Command.openWindow(sectorId);
                break;
            case R.id.buttonGetClimateDefaults:
                Command.getClimateDefaults(sectorId);
                break;
            case R.id.buttonSetClimateDefaults:
                Command.setClimateDefaults(sectorId);
                break;
        }
    }
}
