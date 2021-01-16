package com.klnvch.greenhouseviewer.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.klnvch.greenhousecommon.ui.states.StateActivity;
import com.klnvch.greenhouseviewer.R;
import com.klnvch.greenhouseviewer.firestore.PhoneStateHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StateViewerActivity extends StateActivity {
    private static final String DEFAULT_DEVICE_ID = "test";
    private Disposable disposable = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
    }

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = db.getLatestPhoneStateTime(DEFAULT_DEVICE_ID)
                .flatMap(PhoneStateHelper::read)
                .flatMap(states -> db.insert(states))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
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
