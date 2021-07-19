package com.klnvch.greenhouseviewer.ui.states;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;
import com.klnvch.greenhousecommon.ui.action.ActionActivity;
import com.klnvch.greenhousecommon.ui.settings.DeviceIdDialog;
import com.klnvch.greenhousecommon.ui.states.StateActivity;
import com.klnvch.greenhouseviewer.R;
import com.klnvch.greenhouseviewer.firestore.StateReader;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StateViewerActivity extends StateActivity {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    protected AppDatabase db;
    @Inject
    protected AppSettings settings;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
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

        final long startTime = settings.getStartTime();
        if (startTime > 0) {
            final String deviceId = settings.getDeviceId();

            Single<Integer> phoneStates = db.getLatestPhoneStateTime(deviceId)
                    .map(time -> Math.max(startTime, time))
                    .flatMap(time -> StateReader.readPhoneStates(deviceId, time))
                    .flatMap(db::insertPhoneStates);
            Single<Integer> moduleStates = db.getLatestModuleStateTime(deviceId)
                    .map(time -> Math.max(startTime, time))
                    .flatMap(time -> StateReader.readModuleStates(deviceId, time))
                    .flatMap(db::insertModuleStates);

            compositeDisposable.add(Single.zip(phoneStates, moduleStates, this::sum)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError));
        } else {
            new StartTimeDialog().show(getSupportFragmentManager(), null);
        }
    }

    private Integer sum(Integer i1, Integer i2) {
        return i1 + i2;
    }

    private void onSuccess(Integer size) {
        Toast.makeText(this, "Success: " + size + " items loaded.", Toast.LENGTH_LONG)
                .show();
    }

    private void onError(Throwable e) {
        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Timber.i("User: %s", user.getDisplayName());
            }
        } else {
            if (response != null && response.getError() != null) {
                new AlertDialog.Builder(this)
                        .setMessage(response.getError().getMessage())
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                        .show();
            }
        }
    }
}
