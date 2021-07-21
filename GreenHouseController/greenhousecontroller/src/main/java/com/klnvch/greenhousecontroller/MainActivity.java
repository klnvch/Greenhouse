package com.klnvch.greenhousecontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.klnvch.greenhousecommon.ui.action.ActionActivity;
import com.klnvch.greenhousecommon.ui.settings.DeviceIdDialog;
import com.klnvch.greenhousecommon.ui.states.StateActivity;
import com.klnvch.greenhousecontroller.databinding.ActivityMainBinding;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HasSupportFragmentInjector {
    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ActivityMainBinding binding;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            // Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        }

        startService(new Intent(this, MainService.class));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.buttonState.setOnClickListener(
                v -> startActivity(new Intent(this, StateActivity.class)));
        binding.buttonActions.setOnClickListener(
                v -> startActivity(new Intent(this, ActionActivity.class)));
        binding.buttonExit.setOnClickListener(this);
        binding.buttonInfo.setOnClickListener(this);
        binding.commandGetData.setOnClickListener(this);
        binding.commandSetTime.setOnClickListener(this);
        binding.commandGetGlobalLimits.setOnClickListener(this);
        binding.commandSetGlobalLimits.setOnClickListener(this);
        binding.commandStartVentilation.setOnClickListener(this);
        binding.commandSector0.setOnClickListener(this);
        binding.commandSector1.setOnClickListener(this);
        binding.commandSector2.setOnClickListener(this);
        binding.commandCustom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonExit) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.exit_confirmation_msg)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        stopService(new Intent(this, MainService.class));
                        finish();
                    })
                    .show();
        } else if (v.getId() == R.id.buttonInfo) {
            startActivity(new Intent(this, InfoActivity.class));
        } else if (v.getId() == R.id.command_get_data) {
            Command.getData();
        } else if (v.getId() == R.id.command_set_time) {
            Command.setTime();
        } else if (v.getId() == R.id.command_get_global_limits) {
            Command.getGlobalLimits();
        } else if (v.getId() == R.id.command_set_global_limits) {
            Command.setGlobalLimits();
        } else if (v.getId() == R.id.command_start_ventilation) {
            Command.startVentilation();
        } else if (v.getId() == R.id.command_sector0) {
            SectorActivity.startActivity(this, 0);
        } else if (v.getId() == R.id.command_sector1) {
            SectorActivity.startActivity(this, 1);
        } else if (v.getId() == R.id.command_sector2) {
            SectorActivity.startActivity(this, 2);
        } else if (v.getId() == R.id.command_custom) {
            String command = binding.customCommandValue.getText().toString().trim();
            Command.sendCommand(command);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_device_id) {
            new DeviceIdDialog().show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
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
