package com.klnvch.greenhouseviewer.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;
import com.klnvch.greenhousecommon.ui.action.ActionActivity;
import com.klnvch.greenhousecommon.ui.settings.DeviceIdDialog;
import com.klnvch.greenhousecommon.ui.states.StateActivity;
import com.klnvch.greenhouseviewer.R;
import com.klnvch.greenhouseviewer.firestore.StateReader;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class StateViewerActivity extends StateActivity {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    protected AppDatabase db;
    @Inject
    protected AppSettings settings;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_state, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refresh();
            return true;
        } else if (item.getItemId() == R.id.action_device_id) {
            new DeviceIdDialog().show(getSupportFragmentManager(), null);
            return true;
        } else if (item.getItemId() == R.id.action_actions) {
            startActivity(new Intent(this, ActionActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        compositeDisposable.clear();

        final String deviceId = settings.getDeviceId();

        Single<Integer> phoneStates = db.getLatestPhoneStateTime(deviceId)
                .flatMap(time -> StateReader.readPhoneStates(deviceId, time))
                .flatMap(db::insertPhoneStates);
        Single<Integer> moduleStates = db.getLatestModuleStateTime(deviceId)
                .flatMap(time -> StateReader.readModuleStates(deviceId, time))
                .flatMap(db::insertModuleStates);
        compositeDisposable.add(Single.zip(phoneStates, moduleStates, this::sum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError));
    }

    private Integer sum(Integer i1, Integer i2) {
        return i1 + i2;
    }

    private void onSuccess(Integer size) {
        Toast.makeText(this, "Success: " + size + " items loaded.", Toast.LENGTH_LONG)
                .show();
    }

    private void onError(Throwable throwable) {
        Toast.makeText(this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG)
                .show();
    }
}
