package com.klnvch.greenhousecommon.ui.states;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.databinding.ActivityStateBinding;
import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;
import com.klnvch.greenhousecommon.ui.chart.ChartActivity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StateActivity extends AppCompatActivity implements StateHolderInterface, OnActionListener {
    private final Collection<PhoneStateInterface> phoneStateInterfaces
            = Collections.synchronizedCollection(new HashSet<>());
    private final Collection<ModuleStateInterface> moduleStateInterfaces
            = Collections.synchronizedCollection(new HashSet<>());
    private final CompositeDisposable disposable = new CompositeDisposable();
    protected AppDatabase db = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStateBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_state);
        binding.setListener(this);
        db = AppDatabase.getInstance(this);
    }

    @Override
    protected void onResume() {
        String deviceId = "test";
        disposable.add(db.phoneStateDao().getLatestStates(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updatePhoneStates, Timber::e));
        disposable.add(db.moduleStateDao().getLatestStates(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateModuleStates, Timber::e));
        super.onResume();
    }

    @Override
    protected void onPause() {
        disposable.clear();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        phoneStateInterfaces.clear();
        super.onDestroy();
    }

    @Override
    public void addInterface(PhoneStateInterface stateInterface) {
        phoneStateInterfaces.add(stateInterface);
    }

    @Override
    public void removeInterface(PhoneStateInterface stateInterface) {
        phoneStateInterfaces.remove(stateInterface);
    }

    @Override
    public void addInterface(ModuleStateInterface stateInterface) {
        moduleStateInterfaces.add(stateInterface);
    }

    @Override
    public void removeInterface(ModuleStateInterface stateInterface) {
        moduleStateInterfaces.remove(stateInterface);
    }

    private void updatePhoneStates(List<PhoneState> states) {
        for (PhoneStateInterface phoneStateInterface : phoneStateInterfaces) {
            phoneStateInterface.update(states);
        }
    }

    private void updateModuleStates(List<ModuleState> states) {
        for (ModuleStateInterface moduleStateInterfaces : moduleStateInterfaces) {
            moduleStateInterfaces.update(states);
        }
    }

    @Override
    public void onPhoneStemItemClicked() {
        startActivity(new Intent(this, ChartActivity.class));
    }
}
