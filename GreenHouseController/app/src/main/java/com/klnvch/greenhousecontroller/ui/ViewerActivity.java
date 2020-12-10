package com.klnvch.greenhousecontroller.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecontroller.R;
import com.klnvch.greenhousecontroller.databinding.ActivityViewerBinding;
import com.klnvch.greenhousecontroller.logs.LogsActivity;
import com.klnvch.greenhousecontroller.utils.FireStoreUtils;

public class ViewerActivity extends AppCompatActivity {
    private ActivityViewerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewer);
        binding.fetchButton.setOnClickListener(v -> fetch());
        binding.combineButton.setOnClickListener(v -> combine());
    }

    private void fetch() {
        FireStoreUtils.readInfoFromFireStore(this);
    }

    private void combine() {
        startActivity(new Intent(this, LogsActivity.class));
    }
}
