package com.klnvch.greenhousecontroller.bluetooth;

public interface OnMessageListener {
    void onMessage(String msg);

    void onError(BluetoothException throwable);
}
