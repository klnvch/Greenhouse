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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;
import com.klnvch.greenhousecommon.models.Action;
import com.klnvch.greenhousecommon.models.BluetoothState;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;
import com.klnvch.greenhousecontroller.bluetooth.BluetoothConnectThread;
import com.klnvch.greenhousecontroller.bluetooth.BluetoothException;
import com.klnvch.greenhousecontroller.bluetooth.BluetoothRestartCounter;
import com.klnvch.greenhousecontroller.bluetooth.OnMessageListener;
import com.klnvch.greenhousecontroller.firestore.StateWriter;
import com.klnvch.greenhousecontroller.logs.CustomTimberTree;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class MainService extends Service implements OnMessageListener {
    private static final int PHONE_DATA_UPLOAD_TIMEOUT_MINUTES = 10;
    private static final int MODULE_DATA_UPLOAD_TIMEOUT_MINUTES = 5;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final PublishSubject<ModuleState> moduleStateSubject = PublishSubject.create();
    @Inject
    protected AppDatabase db;
    @Inject
    protected AppSettings settings;
    private BluetoothConnectThread connectThread;
    private Handler threadHandler;
    private BluetoothRestartCounter restartCounter = null;
    private BluetoothException bluetoothException = null;
    private PhoneStatusManager phoneStatusManager;
    private StateWriter stateWriter;

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

        phoneStatusManager = PhoneStatusManager.init(this.getApplicationContext());

        restartCounter = BluetoothRestartCounter.getInstance();

        stateWriter = new StateWriter(FirebaseFirestore.getInstance());

        CustomTimberTree.plant();

        compositeDisposable.add(Observable
                .interval(0,PHONE_DATA_UPLOAD_TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getPhoneState(), Timber::e));

        compositeDisposable.add(moduleStateSubject
                .throttleLatest(MODULE_DATA_UPLOAD_TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveModuleState, Timber::e));
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
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
        connectThread = new BluetoothConnectThread(threadHandler, settings.getDeviceAddress());
        connectThread.start();
    }

    @Override
    public void onMessage(String msg) {
        this.bluetoothException = null;
        try {
            ModuleState moduleState = new Gson().fromJson(msg, ModuleState.class);
            moduleState.setTime(System.currentTimeMillis());
            moduleStateSubject.onNext(moduleState);
            return;
        } catch (Exception e) {
            Timber.d("onMessage: %s", e.getMessage());
        }
        if (!TextUtils.isEmpty(msg)) {
            String deviceId = settings.getDeviceId();
            if (msg.startsWith("Command: ")) {
                msg = msg.replace("Command: ", "");
                String[] answer = msg.split(",");
                if (answer.length == 2) {
                    try {
                        int result = Integer.parseInt(answer[0]);
                        int state = result == 1 ? Action.SUCCESS : Action.FAIL;
                        long time = Long.parseLong(answer[1]);
                        db.actionDao().updateState(deviceId, time, state)
                                .subscribeOn(Schedulers.io())
                                .onErrorComplete()
                                .subscribe();
                    } catch (Exception e) {
                        Timber.e("%s", e.getMessage());
                    }
                }

            }
        }
    }

    private void saveModuleState(ModuleState moduleState) {
        db.insert(moduleState);
        stateWriter.save(moduleState);
    }

    @Override
    public void onError(BluetoothException bluetoothException) {
        this.bluetoothException = bluetoothException;
        restartCounter.start(this::startBluetooth);
    }

    private void getPhoneState() {
        final String deviceId = settings.getDeviceId();

        PhoneState phoneState = new PhoneState();
        phoneState.setDeviceId(deviceId);
        phoneState.setTime(System.currentTimeMillis());
        phoneState.setCharging(phoneStatusManager.isBatteryIsCharging());
        phoneState.setBatteryLevel(phoneStatusManager.getBatteryLevel());
        phoneState.setNetworkStrength(phoneStatusManager.getCellularNetworkStrength());

        PhoneStatusManager.NetworkUsage deviceNetworkUsage = phoneStatusManager.getDeviceNetworkUsage();
        if (deviceNetworkUsage != null) {
            phoneState.setRxDeviceMobile(deviceNetworkUsage.rxMobile);
            phoneState.setTxDeviceMobile(deviceNetworkUsage.txMobile);
            phoneState.setRxDeviceWifi(deviceNetworkUsage.rxWifi);
            phoneState.setTxDeviceWifi(deviceNetworkUsage.txWifi);
        }
        PhoneStatusManager.NetworkUsage packageNetworkUsage = phoneStatusManager.getPackageNetworkUsage();
        if (packageNetworkUsage != null) {
            phoneState.setRxAppMobile(packageNetworkUsage.rxMobile);
            phoneState.setTxAppMobile(packageNetworkUsage.txMobile);
            phoneState.setRxAppWifi(packageNetworkUsage.rxWifi);
            phoneState.setTxAppWifi(packageNetworkUsage.txWifi);
        }

        if (bluetoothException == null) {
            phoneState.setBluetoothState(BluetoothState.CONNECTED);
        } else {
            phoneState.setBluetoothState(bluetoothException.getBluetoothState());
            phoneState.setBluetoothError(bluetoothException.getMessage());
        }

        // save
        stateWriter.save(phoneState);
        db.insert(phoneState);
    }
}
