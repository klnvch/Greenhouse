package com.klnvch.greenhousecontroller.bluetooth;

public class BluetoothException extends Throwable {
    private final int bluetoothState;

    public BluetoothException(int bluetoothState) {
        this.bluetoothState = bluetoothState;
    }

    public BluetoothException(int bluetoothState, String message) {
        super(message);
        this.bluetoothState = bluetoothState;
    }

    public BluetoothException(int bluetoothState, Throwable throwable) {
        super(throwable);
        this.bluetoothState = bluetoothState;
    }

    public int getBluetoothState() {
        return bluetoothState;
    }
}
