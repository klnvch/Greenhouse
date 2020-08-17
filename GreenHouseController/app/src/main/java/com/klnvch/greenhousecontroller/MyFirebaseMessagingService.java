package com.klnvch.greenhousecontroller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Firebase";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        String deviceId = AppSettings.getInstance(this).getDeviceId();
        FireStoreUtils.saveFirebaseToken(deviceId, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String command = null;

        Map<String, String> data = remoteMessage.getData();
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + data);
            command = data.get("command");
        }

        if (command != null && remoteMessage.getNotification() != null) {
            command = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + command);
        }

        Command.sendCommand(command);
    }
}
