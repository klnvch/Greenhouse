package com.klnvch.greenhousecontroller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import java.util.List;

public class PhoneStatusManager {
    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
    private final TelephonyManager telephonyManager;
    private final NetworkStatsManager networkStatsManager;
    private final Context context;
    private final String subscriberId;
    private int uid;

    @SuppressLint("StaticFieldLeak")
    private static PhoneStatusManager instance;

    @SuppressLint("HardwareIds")
    private PhoneStatusManager(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            uid = -1;
        }

        if (telephonyManager != null) {
            subscriberId = telephonyManager.getSubscriberId();
        } else {
            subscriberId = null;
        }
    }

    public static PhoneStatusManager init(Context context) {
        if (instance == null) {
            instance = new PhoneStatusManager(context);
        }
        return instance;
    }

    public static PhoneStatusManager require() {
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("PhoneStatusManager not initialized.");
    }

    public int getCellularNetworkStrength() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
                for (CellInfo cellInfo : allCellInfo) {
                    if (cellInfo.isRegistered()) {
                        if (cellInfo instanceof CellInfoGsm) {
                            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                            CellSignalStrengthGsm signalStrength = cellInfoGsm.getCellSignalStrength();
                            return signalStrength.getDbm();
                        } else if (cellInfo instanceof CellInfoWcdma) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                            CellSignalStrengthWcdma signalStrength = cellInfoWcdma.getCellSignalStrength();
                            return signalStrength.getDbm();
                        } else if (cellInfo instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            CellSignalStrengthLte signalStrength = cellInfoLte.getCellSignalStrength();
                            return signalStrength.getDbm();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private Intent getBatteryStatus() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return context.registerReceiver(null, intentFilter);
    }

    public boolean isBatteryIsCharging() {
        Intent batteryStatus = getBatteryStatus();
        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
        }
        return false;
    }

    public int getBatteryLevel() {
        Intent batteryStatus = getBatteryStatus();
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            return (int) (level * 100 / (float) scale);
        }
        return 0;
    }

    public NetworkUsage getDeviceNetworkUsage() {
        try {
            long now = System.currentTimeMillis();

            NetworkStats.Bucket bucketMobile = networkStatsManager.querySummaryForDevice(
                    ConnectivityManager.TYPE_MOBILE,
                    subscriberId,
                    now - ONE_DAY_MILLIS,
                    now);
            long rxMobile = bucketMobile.getRxBytes();
            long txMobile = bucketMobile.getTxBytes();

            NetworkStats.Bucket bucketWifi = networkStatsManager.querySummaryForDevice(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    now - ONE_DAY_MILLIS,
                    now);
            long rxWifi = bucketWifi.getRxBytes();
            long txWifi = bucketWifi.getTxBytes();

            return new NetworkUsage(rxMobile, txMobile, rxWifi, txWifi);
        } catch (RemoteException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NetworkUsage getPackageNetworkUsage() {
        try {
            long now = System.currentTimeMillis();

            NetworkStats networkStatsMobile = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    subscriberId,
                    now - ONE_DAY_MILLIS,
                    now,
                    uid);

            long rxMobileBytes = 0L;
            long txMobileBytes = 0L;
            NetworkStats.Bucket bucketMobile = new NetworkStats.Bucket();
            while (networkStatsMobile.hasNextBucket()) {
                networkStatsMobile.getNextBucket(bucketMobile);
                rxMobileBytes += bucketMobile.getRxBytes();
                txMobileBytes += bucketMobile.getTxBytes();
            }
            networkStatsMobile.close();

            NetworkStats networkStatsWifi = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    now - ONE_DAY_MILLIS,
                    now,
                    uid);

            long rxWifiBytes = 0L;
            long txWifiBytes = 0L;
            NetworkStats.Bucket bucketWifi = new NetworkStats.Bucket();
            while (networkStatsWifi.hasNextBucket()) {
                networkStatsWifi.getNextBucket(bucketWifi);
                rxWifiBytes += bucketWifi.getRxBytes();
                txWifiBytes += bucketWifi.getTxBytes();
            }
            networkStatsWifi.close();

            return new NetworkUsage(rxMobileBytes, txMobileBytes, rxWifiBytes, txWifiBytes);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class NetworkUsage {
        public final long rxMobile;
        public final long txMobile;
        public final long rxWifi;
        public final long txWifi;

        public NetworkUsage(long rxMobile, long txMobile, long rxWifi, long txWifi) {
            this.rxMobile = rxMobile;
            this.txMobile = txMobile;
            this.rxWifi = rxWifi;
            this.txWifi = txWifi;
        }
    }
}
