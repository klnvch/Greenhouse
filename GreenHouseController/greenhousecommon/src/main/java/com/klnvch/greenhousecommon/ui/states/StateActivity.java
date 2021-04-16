package com.klnvch.greenhousecommon.ui.states;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.databinding.ActivityStateBinding;
import com.klnvch.greenhousecommon.ui.chart.ChartActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class StateActivity extends AppCompatActivity implements OnActionListener, HasSupportFragmentInjector {

    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStateBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_state);
        binding.setListener(this);
    }

    @Override
    public void onPhoneStemItemClicked() {
        startActivity(new Intent(this, ChartActivity.class));
    }

    @Override
    public void onModuleStateItemClicked() {

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
