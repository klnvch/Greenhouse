package com.klnvch.greenhousecontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CommandDetailsActivity extends AppCompatActivity {
    private static final String KEY_COMMAND_ID = "KEY_COMMAND_ID";
    private static final String KEY_SECTOR_ID = "KEY_SECTOR_ID";

    private int commandId;
    private int sectorId;

    static Intent getLaunchIntent(Context context, int commandId, int sectorId) {
        return new Intent(context, CommandDetailsActivity.class)
                .putExtra(KEY_COMMAND_ID, commandId)
                .putExtra(KEY_SECTOR_ID, sectorId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            commandId = getIntent().getIntExtra(KEY_COMMAND_ID, -1);
            sectorId = getIntent().getIntExtra(KEY_SECTOR_ID, -1);
        }
    }
}
