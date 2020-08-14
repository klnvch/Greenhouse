package com.klnvch.greenhousecontroller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.databinding.ActivityLogsBinding;

public class LogsActivity extends AppCompatActivity {
    private ActivityLogsBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logs);
    }
}
