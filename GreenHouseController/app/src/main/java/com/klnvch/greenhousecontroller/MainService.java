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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.klnvch.greenhousecontroller.logs.CustomTimberTree;
import com.klnvch.greenhousecontroller.models.AppDatabase;
import com.klnvch.greenhousecontroller.models.Data;
import com.klnvch.greenhousecontroller.models.Info;
import com.klnvch.greenhousecontroller.models.PhoneData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainService extends Service implements OnMessageListener {
    private static final String KEY_DEVICE_ADDRESS = "KEY_DEVICE_ADDRESS";
    private static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";

    private BluetoothConnectThread connectThread;
    private String deviceAddress;
    private String deviceId;
    private Handler threadHandler;
    private AppDatabase db;
    private BluetoothRestartCounter restartCounter = null;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    static void start(Context context, String deviceAddress, String deviceId) {
        context.startService(new Intent(context, MainService.class)
                .putExtra(KEY_DEVICE_ADDRESS, deviceAddress)
                .putExtra(KEY_DEVICE_ID, deviceId));
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
                            FireStoreUtils.saveFirebaseToken(deviceId, task.getResult().getToken());
                        }
                    } else {
                        if (task.getException() != null) {
                            Timber.e("getInstanceId failed: %s", task.getException().getMessage());
                        }
                    }
                });

        PhoneStatusManager.init(this.getApplicationContext());

        restartCounter = BluetoothRestartCounter.getInstance();

        db = AppDatabase.getInstance(this);
        CustomTimberTree.plant(this);

        compositeDisposable.add(Observable.interval(1, 10, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    PhoneData phoneData = PhoneData.create();
                    db.insert(phoneData);
                    FireStoreUtils.saveToFireStore(deviceId, phoneData);
                }, throwable -> Timber.e("Phone data error: %s", throwable.getMessage())));
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        deviceAddress = intent.getStringExtra(KEY_DEVICE_ADDRESS);
        deviceId = intent.getStringExtra(KEY_DEVICE_ID);
        restartCounter.reset();
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
        restartCounter.reset();
        if (connectThread != null) {
            connectThread.cancel();
        }
        compositeDisposable.clear();
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
                Data data = new Data(msg);
                FireStoreUtils.saveToFireStore(deviceId, data);
                db.dataDao().insertAll(data)
                        .subscribeOn(Schedulers.io())
                        .onErrorComplete()
                        .subscribe();
            } else {
                Info info = new Info(msg);
                FireStoreUtils.saveToFireStore(deviceId, info);
                db.infoDao().insertAll(info)
                        .subscribeOn(Schedulers.io())
                        .onErrorComplete()
                        .subscribe();
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        restartCounter.start(this::startBluetooth);
    }
}
