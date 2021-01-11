package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StateActivity extends AppCompatActivity implements StateHolderInterface {
    private final Collection<PhoneStateInterface> phoneStateInterfaces
            = Collections.synchronizedCollection(new HashSet<>());
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_state);
    }

    @Override
    protected void onResume() {
        String deviceId = "test";
        disposable = AppDatabase.getInstance(this).phoneStateDao().getLatestPhoneStates(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::update, Timber::e);
        super.onResume();
    }

    @Override
    protected void onPause() {
        disposable.dispose();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        phoneStateInterfaces.clear();
        super.onDestroy();
    }

    @Override
    public void addPhoneStateInterface(PhoneStateInterface phoneStateInterface) {
        phoneStateInterfaces.add(phoneStateInterface);
    }

    @Override
    public void removePhoneStateInterface(PhoneStateInterface phoneStateInterface) {
        phoneStateInterfaces.remove(phoneStateInterface);
    }

    private void update(List<PhoneState> states) {
        for (PhoneStateInterface phoneStateInterface : phoneStateInterfaces) {
            phoneStateInterface.update(states);
        }
    }
}
