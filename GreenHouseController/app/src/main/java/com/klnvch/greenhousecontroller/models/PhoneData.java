package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.klnvch.greenhousecontroller.PhoneStatusManager;

import java.util.HashMap;
import java.util.Map;

@Entity(primaryKeys = {"id"}, tableName = "phone")
public class PhoneData extends FireStoreData {
    private String networkStrength;
    private String isCharging;
    private String batteryLevel;

    public PhoneData() {

    }

    @NonNull
    public static PhoneData create() {
        PhoneData phoneData = new PhoneData();
        PhoneStatusManager phoneStatusManager = PhoneStatusManager.require();
        phoneData.networkStrength = Integer.toString(phoneStatusManager.getCellularNetworkStrength());
        phoneData.isCharging = Boolean.toString(phoneStatusManager.isBatteryIsCharging());
        phoneData.batteryLevel = Integer.toString(phoneStatusManager.getBatteryLevel());
        return phoneData;
    }

    public String getNetworkStrength() {
        return networkStrength;
    }

    public void setNetworkStrength(String networkStrength) {
        this.networkStrength = networkStrength;
    }

    public String getIsCharging() {
        return isCharging;
    }

    public void setIsCharging(String isCharging) {
        this.isCharging = isCharging;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @NonNull
    @Override
    public String getCollectionPath() {
        return "phone";
    }

    @NonNull
    @Override
    public Map<String, Object> toRow() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("networkStrength", networkStrength);
        result.put("isCharging", isCharging);
        result.put("batteryLevel", batteryLevel);
        return result;
    }

    @Override
    public String toString() {
        return "PhoneData{" +
                "networkStrength='" + networkStrength + '\'' +
                ", isCharging='" + isCharging + '\'' +
                ", batteryLevel='" + batteryLevel + '\'' +
                '}';
    }
}
