package com.klnvch.greenhousecontroller.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.MainActivity;
import com.klnvch.greenhousecontroller.R;
import com.klnvch.greenhousecontroller.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStartBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        binding.buttonController.setOnClickListener(
                v -> startActivity(new Intent(this, MainActivity.class)));
    }
}
