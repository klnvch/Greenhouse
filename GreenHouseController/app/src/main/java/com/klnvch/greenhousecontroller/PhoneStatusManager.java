package com.klnvch.greenhousecontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;

import java.util.List;
import java.util.Map;

class PhoneStatusManager {
    private final TelephonyManager telephonyManager;
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static PhoneStatusManager instance;

    PhoneStatusManager(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static void init(Context context) {
        instance = new PhoneStatusManager(context);
    }

    public static void addData(Map<String, Object> data) {
        if (instance != null) {
            data.put("networkStrength", instance.getCellularNetworkStrength());
            data.put("isCharging", instance.isBatteryIsCharging());
            data.put("batteryLevel", instance.getBatteryLevel());
        }
        throw new IllegalStateException("PhoneStatusManager not initialized.");
    }

    @SuppressLint("MissingPermission")
    private Integer getCellularNetworkStrength() {
        try {
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
                    return cellSignalStrengthGsm.getDbm();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Intent getBatteryStatus() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return context.registerReceiver(null, intentFilter);
    }

    private Boolean isBatteryIsCharging() {
        Intent batteryStatus = getBatteryStatus();
        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
        }
        return null;
    }

    private Integer getBatteryLevel() {
        Intent batteryStatus = getBatteryStatus();
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            return (int) (level * 100 / (float) scale);
        }
        return null;
    }
}
