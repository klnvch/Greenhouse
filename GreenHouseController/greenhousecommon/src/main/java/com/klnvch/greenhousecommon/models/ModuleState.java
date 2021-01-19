package com.klnvch.greenhousecommon.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"deviceId", "time"}, tableName = "moduleState")
public class ModuleState {
    @NonNull
    private String deviceId = "test";
    private long time;
    private long mainModuleTime;
    private long waterModuleLastAccess;
    private long climateModuleLastAccess;
    private int waterLevel;
    private float temperature;
    private float humidity;
    private int lightLevel;

    public ModuleState() {
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

    public long getMainModuleTime() {
        return mainModuleTime;
    }

    public void setMainModuleTime(long mainModuleTime) {
        this.mainModuleTime = mainModuleTime;
    }

    public long getWaterModuleLastAccess() {
        return waterModuleLastAccess;
    }

    public void setWaterModuleLastAccess(long waterModuleLastAccess) {
        this.waterModuleLastAccess = waterModuleLastAccess;
    }

    public long getClimateModuleLastAccess() {
        return climateModuleLastAccess;
    }

    public void setClimateModuleLastAccess(long climateModuleLastAccess) {
        this.climateModuleLastAccess = climateModuleLastAccess;
    }
    
    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }
}
