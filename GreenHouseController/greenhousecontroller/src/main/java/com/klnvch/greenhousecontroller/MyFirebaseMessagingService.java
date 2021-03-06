package com.klnvch.greenhousecontroller;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.klnvch.greenhousecontroller.utils.FireStoreUtils;

import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    protected com.klnvch.greenhousecommon.db.AppSettings settings;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Timber.d("Refreshed token: %s", token);
        String deviceId = settings.getDeviceId();
        FireStoreUtils.saveFirebaseToken(deviceId, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Timber.d("From: %s", remoteMessage.getFrom());
        String command = null;

        Map<String, String> data = remoteMessage.getData();
        if (remoteMessage.getData().size() > 0) {
            Timber.d("Message data payload: %s", data);
            command = data.get("command");
        }

        if (command != null && remoteMessage.getNotification() != null) {
            command = remoteMessage.getNotification().getBody();
            Timber.d("Message Notification Body: %s", command);
        }

        Command.sendCommand(command);
    }
}
