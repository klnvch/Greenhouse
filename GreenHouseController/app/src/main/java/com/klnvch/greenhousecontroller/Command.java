package com.klnvch.greenhousecontroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/*
  0                                               - get data
  1, yy,mm,dd,hh,mm,ss                            - set time
  2                                               - get global limits
  3, minVoltage,fanVoltage,waterLevel,fanTimeout  - set global limits
  4, sectorId,timeoutSeconds                      - start watering
  5, sectorId                                     - get watering defaults
  6, sectorId,minNorth,minSouth,timeoutSeconds    - set watering defaults
  7, timeoutSeconds                               - turn on global fans
  8, sectorId,angle                               - move window
  9, sectorId,timeoutSeconds                      - start fan
  10,sectorId                                     - get climate defaults
  11,sectorId, windowTemperature, fanTemperature  - set climate defaults
  12                                              - help
 */
class Command {
    private static final PublishSubject<String> commandQueue = PublishSubject.create();

    static void getData() {
        sendCommand("0");
    }

    static void setTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy,M,d,H,m,s", Locale.getDefault());
        sendCommand("1," + dateFormat.format(new Date()));
    }

    static void getGlobalLimits() {
        sendCommand("2");
    }

    static void setGlobalLimits() {
        sendCommand("3,12,13,120,60");
    }

    static void startWatering(int sectorId) {
        sendCommand("4," + sectorId + ",60");
    }

    static void getWateringDefaults(int sectorId) {
        sendCommand("5," + sectorId);
    }

    static void setWateringDefaults(int sectorId) {
        sendCommand("6," + sectorId + ",600,600,300");
    }

    static void startVentilation() {
        sendCommand("7,300");
    }

    static void closeWindow(int sectorId) {
        sendCommand("8," + sectorId + ",0");
    }

    static void openWindow(int sectorId) {
        sendCommand("8," + sectorId + ",180");
    }

    static void startVentilation(int sectorId) {
        sendCommand("9," + sectorId + ",300");
    }

    static void getClimateDefaults(int sectorId) {
        sendCommand("10," + sectorId);
    }

    static void setClimateDefaults(int sectorId) {
        sendCommand("11," + sectorId + ",24,26");
    }

    @NonNull
    static Observable<String> getCommandQueue() {
        return commandQueue.debounce(5, TimeUnit.SECONDS);
    }

    static void sendCommand(@Nullable String command) {
        if (command != null) {
            commandQueue.onNext(command);
        }
    }
}
