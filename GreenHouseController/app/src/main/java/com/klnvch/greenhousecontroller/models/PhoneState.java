package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"deviceId", "time"}, tableName = "phoneState")
public class PhoneState {
    @NonNull
    private String deviceId = "test";
    private long time;
    private boolean isCharging;
    private int batteryLevel;
    private int networkStrength;
    private boolean isBluetoothActive;

    public PhoneState() {
    }

    @NotNull
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@NonNull String deviceId) {
        this.deviceId = deviceId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getNetworkStrength() {
        return networkStrength;
    }

    public void setNetworkStrength(int networkStrength) {
        this.networkStrength = networkStrength;
    }

    public boolean isBluetoothActive() {
        return isBluetoothActive;
    }

    public void setBluetoothActive(boolean bluetoothActive) {
        isBluetoothActive = bluetoothActive;
    }
}
