package com.klnvch.greenhousecommon.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"deviceId", "time"}, tableName = "phoneState")
public class PhoneState {
    @NonNull
    private String deviceId = "test";
    private long time;
    private boolean isCharging;
    private int batteryLevel;
    private int networkStrength;
    private int bluetoothState;
    private String bluetoothError;
    private long rxDeviceMobile;
    private long txDeviceMobile;
    private long rxDeviceWifi;
    private long txDeviceWifi;
    private long rxAppMobile;
    private long txAppMobile;
    private long rxAppWifi;
    private long txAppWifi;

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

    @JsonProperty(value="isCharging")
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

    public int getBluetoothState() {
        return bluetoothState;
    }

    public void setBluetoothState(int bluetoothState) {
        this.bluetoothState = bluetoothState;
    }

    public String getBluetoothError() {
        return bluetoothError;
    }

    public void setBluetoothError(String bluetoothError) {
        this.bluetoothError = bluetoothError;
    }

    public long getRxDeviceMobile() {
        return rxDeviceMobile;
    }

    public void setRxDeviceMobile(long rxDeviceMobile) {
        this.rxDeviceMobile = rxDeviceMobile;
    }

    public long getTxDeviceMobile() {
        return txDeviceMobile;
    }

    public void setTxDeviceMobile(long txDeviceMobile) {
        this.txDeviceMobile = txDeviceMobile;
    }

    public long getRxDeviceWifi() {
        return rxDeviceWifi;
    }

    public void setRxDeviceWifi(long rxDeviceWifi) {
        this.rxDeviceWifi = rxDeviceWifi;
    }

    public long getTxDeviceWifi() {
        return txDeviceWifi;
    }

    public void setTxDeviceWifi(long txDeviceWifi) {
        this.txDeviceWifi = txDeviceWifi;
    }

    public long getRxAppMobile() {
        return rxAppMobile;
    }

    public void setRxAppMobile(long rxAppMobile) {
        this.rxAppMobile = rxAppMobile;
    }

    public long getTxAppMobile() {
        return txAppMobile;
    }

    public void setTxAppMobile(long txAppMobile) {
        this.txAppMobile = txAppMobile;
    }

    public long getRxAppWifi() {
        return rxAppWifi;
    }

    public void setRxAppWifi(long rxAppWifi) {
        this.rxAppWifi = rxAppWifi;
    }

    public long getTxAppWifi() {
        return txAppWifi;
    }

    public void setTxAppWifi(long txAppWifi) {
        this.txAppWifi = txAppWifi;
    }
}
