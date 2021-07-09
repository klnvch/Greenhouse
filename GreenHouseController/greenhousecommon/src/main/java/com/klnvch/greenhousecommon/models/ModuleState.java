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
    private int waterModuleSuccessCount;
    private int waterModuleFailCount;
    private long waterModuleLastAccess;
    private long climateModuleLastAccess;
    private int waterLevel;
    private Float temperature;
    private Float humidity;
    private int lightLevel;
    private int ws1S;
    private int ws1N;
    private int ws2S;
    private int ws2N;
    private int ws3S;
    private int ws3N;

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

    public int getWaterModuleSuccessCount() {
        return waterModuleSuccessCount;
    }

    public void setWaterModuleSuccessCount(int waterModuleSuccessCount) {
        this.waterModuleSuccessCount = waterModuleSuccessCount;
    }

    public int getWaterModuleFailCount() {
        return waterModuleFailCount;
    }

    public void setWaterModuleFailCount(int waterModuleFailCount) {
        this.waterModuleFailCount = waterModuleFailCount;
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

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public int getWs1S() {
        return ws1S;
    }

    public void setWs1S(int ws1S) {
        this.ws1S = ws1S;
    }

    public int getWs1N() {
        return ws1N;
    }

    public void setWs1N(int ws1N) {
        this.ws1N = ws1N;
    }

    public int getWs2S() {
        return ws2S;
    }

    public void setWs2S(int ws2S) {
        this.ws2S = ws2S;
    }

    public int getWs2N() {
        return ws2N;
    }

    public void setWs2N(int ws2N) {
        this.ws2N = ws2N;
    }

    public int getWs3S() {
        return ws3S;
    }

    public void setWs3S(int ws3S) {
        this.ws3S = ws3S;
    }

    public int getWs3N() {
        return ws3N;
    }

    public void setWs3N(int ws3N) {
        this.ws3N = ws3N;
    }
}
