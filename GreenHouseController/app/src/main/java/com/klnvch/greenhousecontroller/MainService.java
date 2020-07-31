package com.klnvch.greenhousecontroller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.klnvch.greenhousecontroller.models.Data;
import com.klnvch.greenhousecontroller.models.Info;

public class MainService extends Service implements OnMessageListener {
    private static final String KEY_DEVICE_ADDRESS = "KEY_DEVICE_ADDRESS";
    private static final String TAG = "MainService";

    private final Handler restartHandler = new Handler();
    private BluetoothConnectThread connectThread;
    private String deviceAddress;
    private Handler threadHandler;

    static void start(Context context, String deviceAddress) {
        context.startService(new Intent(context, MainService.class)
                .putExtra(KEY_DEVICE_ADDRESS, deviceAddress));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
        threadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        onError((Throwable) msg.obj);
                        break;
                    case 1:
                        onMessage((String) msg.obj);
                        break;
                }
            }
        };

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            FireStoreUtils.saveFirebaseToken(task.getResult().getToken());
                        }
                    } else {
                        Log.e(TAG, "getInstanceId failed", task.getException());
                    }
                });

        PhoneStatusManager.init(this.getApplicationContext());
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        deviceAddress = intent.getStringExtra(KEY_DEVICE_ADDRESS);
        restartHandler.removeCallbacksAndMessages(null);
        startBluetooth();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        restartHandler.removeCallbacksAndMessages(null);
        if (connectThread != null) {
            connectThread.cancel();
        }
        super.onDestroy();
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.klnvch.greenhousecontroller";
            String channelName = "Greenhouse Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_settings_black_24dp)
                    .setContentTitle("App is running in the background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(2, notification);
        } else {
            startForeground(1, new Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .build());
        }
    }

    private void startBluetooth() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        connectThread = new BluetoothConnectThread(threadHandler, deviceAddress);
        connectThread.start();
    }

    @Override
    public void onMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        if (!TextUtils.isEmpty(msg)) {
            if (msg.startsWith("Data: ")) {
                FireStoreUtils.saveToFireStore(new Data(msg));
            } else {
                FireStoreUtils.saveToFireStore(new Info(msg));
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
        restartHandler.postDelayed(this::startBluetooth, 10000);
    }
}
