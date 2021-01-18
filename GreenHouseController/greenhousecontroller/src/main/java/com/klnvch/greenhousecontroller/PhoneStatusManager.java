package com.klnvch.greenhousecontroller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
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
    private final TelephonyManager telephonyManager;
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static PhoneStatusManager instance;

    private PhoneStatusManager(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

    @SuppressLint("MissingPermission")
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
}