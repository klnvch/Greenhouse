package com.klnvch.greenhousecontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class BluetoothConnectThread extends Thread {
    private static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb";
    private final BluetoothAdapter bluetoothAdapter;
    private final String deviceAddress;
    private Handler handler;
    private BluetoothSocket socket;
    private BufferedWriter outputStream;
    private Disposable disposable = null;

    BluetoothConnectThread(@NonNull Handler handler, @NonNull String deviceAddress) {
        this.handler = handler;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.deviceAddress = deviceAddress;
    }

    @Override
    public void run() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                BluetoothDevice device;
                try {
                    device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                } catch (Exception e) {
                    onError(e);
                    return;
                }
                if (device == null) {
                    onError("Device creation failed");
                    return;
                }
                try {
                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_ID));
                } catch (IOException e) {
                    onError(e);
                    return;
                }
                if (socket == null) {
                    onError("Socket creation failed");
                    return;
                }

                bluetoothAdapter.cancelDiscovery();

                try {
                    socket.connect();

                    outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    disposable = Command.getCommandQueue().subscribe(this::sendCommand);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        try {
                            String msg = reader.readLine();
                            if (handler != null) {
                                handler.obtainMessage(1, msg).sendToTarget();
                            }
                        } catch (Exception e) {
                            onError(e);
                            break;
                        }
                    }
                } catch (IOException connectException) {
                    onError(connectException);
                    try {
                        socket.close();
                    } catch (IOException closeException) {
                        Timber.e("Can't close socket");
                    }
                }
            } else {
                onError("Bluetooth not enabled");
            }
        } else {
            onError("Bluetooth not available");
        }
    }

    private void onError(@NonNull String msg) {
        onError(new Exception(msg));
    }

    private void onError(@NonNull Throwable throwable) {
        if (handler != null) {
            handler.obtainMessage(0, throwable).sendToTarget();
        } else {
            Timber.e("Could not close the connect socket: %s.", throwable.getMessage());
        }
    }

    void cancel() {
        handler = null;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Timber.e("Could not close the connect socket: %s.", e.getMessage());
            }
            socket = null;
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void sendCommand(String command) {
        if (outputStream != null) {
            try {
                outputStream.write(command);
                outputStream.newLine();
                outputStream.flush();
            } catch (IOException e) {
                onError(e);
            }
        }
    }
}
