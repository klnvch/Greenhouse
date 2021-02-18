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
import com.google.gson.Gson;
import com.klnvch.greenhousecommon.models.BluetoothState;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;
import com.klnvch.greenhousecontroller.bluetooth.BluetoothConnectThread;
import com.klnvch.greenhousecontroller.bluetooth.BluetoothException;
import com.klnvch.greenhousecontroller.bluetooth.BluetoothRestartCounter;
import com.klnvch.greenhousecontroller.bluetooth.OnMessageListener;
import com.klnvch.greenhousecontroller.firestore.StateWriter;
import com.klnvch.greenhousecontroller.logs.CustomTimberTree;
import com.klnvch.greenhousecontroller.models.AppDatabase;
import com.klnvch.greenhousecontroller.models.Data;
import com.klnvch.greenhousecontroller.models.Info;
import com.klnvch.greenhousecontroller.models.PhoneData;
import com.klnvch.greenhousecontroller.utils.FireStoreUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
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
    private String deviceId = null;
    private Handler threadHandler;
    private AppDatabase db;
    @Inject
    protected com.klnvch.greenhousecommon.db.AppDatabase newDb;
    private BluetoothRestartCounter restartCounter = null;
    private BluetoothException bluetoothException = null;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PhoneStatusManager phoneStatusManager;

    static void start(Context context, String deviceAddress, String deviceId) {
        context.startService(new Intent(context, MainService.class)
                .putExtra(KEY_DEVICE_ADDRESS, deviceAddress)
                .putExtra(KEY_DEVICE_ID, deviceId));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
        showNotification();
        threadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        onError((BluetoothException) msg.obj);
                        break;
                    case 1:
                        onMessage((String) msg.obj);
                        break;
                }
            }
        };

        if (deviceId != null) {
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
        }

        phoneStatusManager = PhoneStatusManager.init(this.getApplicationContext());

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

        compositeDisposable.add(Observable.interval(1, 5, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getPhoneState(), Timber::e));
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        if (intent != null) {
            deviceAddress = intent.getStringExtra(KEY_DEVICE_ADDRESS);
            deviceId = intent.getStringExtra(KEY_DEVICE_ID);
        }
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
        this.bluetoothException = null;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        try {
            ModuleState moduleState = new Gson().fromJson(msg, ModuleState.class);
            newDb.moduleStateDao().insert(moduleState).subscribeOn(Schedulers.io()).subscribe();
            StateWriter.save(moduleState);
            return;
        } catch (Exception e) {
            Timber.d("onMessage: %s", e.getMessage());
        }
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
    public void onError(BluetoothException bluetoothException) {
        this.bluetoothException = bluetoothException;
        restartCounter.start(this::startBluetooth);
    }

    private void getPhoneState() {
        PhoneState phoneState = new PhoneState();
        phoneState.setDeviceId("test");
        phoneState.setTime(System.currentTimeMillis());
        phoneState.setCharging(phoneStatusManager.isBatteryIsCharging());
        phoneState.setBatteryLevel(phoneStatusManager.getBatteryLevel());
        phoneState.setNetworkStrength(phoneStatusManager.getCellularNetworkStrength());
        if (bluetoothException == null) {
            phoneState.setBluetoothState(BluetoothState.CONNECTED);
        } else {
            phoneState.setBluetoothState(bluetoothException.getBluetoothState());
            phoneState.setBluetoothError(bluetoothException.getMessage());
        }

        // save
        newDb.phoneStateDao().insert(phoneState).subscribeOn(Schedulers.io()).subscribe();
        StateWriter.save(phoneState);
    }
}
