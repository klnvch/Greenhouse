package com.klnvch.greenhousecontroller.models;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.klnvch.greenhousecontroller.PhoneStatusManager;

import java.util.HashMap;
import java.util.Map;

/**
 * void logToStream(Stream &stream) {
 * stream << dateTime << COMMA << distance << COMMA
 * << temperature << COMMA << humidity << COMMA << light << COMMA
 * << solarVoltage << COMMA << batteryVoltage << COMMA
 * << waterData.south1 << COMMA << waterData.north1 << COMMA
 * << waterData.south2 << COMMA << waterData.north2 << COMMA
 * << waterData.south3 << COMMA << waterData.north3 << COMMA
 * << climateData.t1 << COMMA << climateData.h1 << COMMA << climateData.angle1 << COMMA
 * << climateData.t2 << COMMA << climateData.h2 << COMMA << climateData.angle2 << COMMA
 * << climateData.t3 << COMMA << climateData.h3 << COMMA << climateData.angle3 << COMMA
 * << climateData.angleCommon << isWaterModuleActive << isClimateModuleActive << endl;
 * }
 */
public class Data extends FireStoreData {
    private static final int N = 25;
    private String dateTime;
    private String distance;
    private String temperature;
    private String humidity;
    private String light;
    private String solarVoltage;
    private String batteryVoltage;
    private String s1;
    private String n1;
    private String s2;
    private String n2;
    private String s3;
    private String n3;
    private String t1;
    private String h1;
    private String angle1;
    private String t2;
    private String h2;
    private String angle2;
    private String t3;
    private String h3;
    private String angle3;
    private String angleCommon;
    private String isWaterModuleActive;
    private String isClimateModuleActive;

    private String networkStrength;
    private String isCharging;
    private String batteryLevel;

    public Data(@NonNull String str) {
        str = str.replace("Data: ", "");
        String[] parts = str.split(",");
        if (parts.length == N) {
            dateTime = parts[0];
            distance = parts[1];
            temperature = parts[2];
            humidity = parts[3];
            light = parts[4];
            solarVoltage = parts[5];
            batteryVoltage = parts[6];
            s1 = parts[7];
            n1 = parts[8];
            s2 = parts[9];
            n2 = parts[10];
            s3 = parts[11];
            n3 = parts[12];
            t1 = parts[13];
            h1 = parts[14];
            angle1 = parts[15];
            t2 = parts[16];
            h2 = parts[17];
            angle2 = parts[18];
            t3 = parts[19];
            h3 = parts[20];
            angle3 = parts[21];
            angleCommon = parts[22];
            isWaterModuleActive = parts[23];
            isClimateModuleActive = parts[24];
        }

        PhoneStatusManager phoneStatusManager = PhoneStatusManager.require();
        networkStrength = Integer.toString(phoneStatusManager.getCellularNetworkStrength());
        isCharging = Boolean.toString(phoneStatusManager.isBatteryIsCharging());
        batteryLevel = Integer.toString(phoneStatusManager.getBatteryLevel());
    }

    @NonNull
    @Override
    public String getCollectionPath() {
        return "data";
    }

    @NonNull
    @Override
    public Map<String, Object> toRow() {
        Map<String, Object> result = new HashMap<>();
        result.put("dateTime", dateTime);
        result.put("distance", distance);
        result.put("temperature", temperature);
        result.put("humidity", humidity);
        result.put("light", light);
        result.put("solarVoltage", solarVoltage);
        result.put("batteryVoltage", batteryVoltage);
        result.put("s1", s1);
        result.put("n1", n1);
        result.put("s2", s2);
        result.put("n2", n2);
        result.put("s3", s3);
        result.put("n3", n3);
        result.put("t1", t1);
        result.put("h1", h1);
        result.put("angle1", angle1);
        result.put("t2", t2);
        result.put("h2", h2);
        result.put("angle2", angle2);
        result.put("t3", t3);
        result.put("h3", h3);
        result.put("angle3", angle3);
        result.put("angleCommon", angleCommon);
        result.put("isWaterModuleActive", isWaterModuleActive);
        result.put("isClimateModuleActive", isClimateModuleActive);
        result.put("networkStrength", networkStrength);
        result.put("isCharging", isCharging);
        result.put("batteryLevel", batteryLevel);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return TextUtils.join(",", new String[]{dateTime, distance, temperature, humidity, light, solarVoltage, batteryVoltage, s1, n1, s2, n2, s3, n3, t1, h1, angle1, t2, h2, angle2, t3, h3, angle3, angleCommon, isWaterModuleActive, isClimateModuleActive, networkStrength, isCharging, batteryLevel});
    }
}
