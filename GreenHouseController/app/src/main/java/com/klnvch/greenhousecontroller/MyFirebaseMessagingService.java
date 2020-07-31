package com.klnvch.greenhousecontroller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Firebase";
    private static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";
    private static final String SHARED_PREFERENCES_NAME = "settings";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        String deviceId = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                .getString(KEY_DEVICE_ID, "0");
        FireStoreUtils.saveFirebaseToken(deviceId, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + body);
            Command.sendCommand(body);
        }
    }
}
