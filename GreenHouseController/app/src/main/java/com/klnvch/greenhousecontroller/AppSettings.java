package com.klnvch.greenhousecontroller;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;

class AppSettings {
    private static final String SHARED_PREFERENCES_NAME = "settings";
    private static final String KEY_DEVICE_ADDRESS = "KEY_DEVICE_ADDRESS";
    private static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";

    private final SharedPreferences sharedPreferences;

    private AppSettings(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    synchronized static AppSettings getInstance(@NonNull Context context) {
        return new AppSettings(context.getApplicationContext());
    }

    String getDeviceAddress() {
        return sharedPreferences.getString(KEY_DEVICE_ADDRESS, "98:D3:33:F5:A3:24");
    }

    void setDeviceAddress(String deviceAddress) {
        sharedPreferences.edit().putString(KEY_DEVICE_ADDRESS, deviceAddress).apply();
    }

    String getDeviceId() {
        String id = FirebaseInstanceId.getInstance().getId();
        return sharedPreferences.getString(KEY_DEVICE_ID, id);
    }
}
