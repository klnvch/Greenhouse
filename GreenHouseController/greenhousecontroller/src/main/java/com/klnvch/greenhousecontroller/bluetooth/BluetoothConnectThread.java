package com.klnvch.greenhousecontroller.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.klnvch.greenhousecommon.models.BluetoothState;
import com.klnvch.greenhousecontroller.Command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class BluetoothConnectThread extends Thread {
    private static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb";
    private final BluetoothAdapter bluetoothAdapter;
    private final String deviceAddress;
    private Handler handler;
    private BluetoothSocket socket;
    private BufferedWriter outputStream;
    private Disposable disposable = null;

    public BluetoothConnectThread(@NonNull Handler handler, @NonNull String deviceAddress) {
        this.handler = handler;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.deviceAddress = deviceAddress;
    }

    @Override
    public void run() {
        if (bluetoothAdapter == null) {
            onError(new BluetoothException(BluetoothState.NOT_AVAILABLE));
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            onError(new BluetoothException(BluetoothState.NOT_ENABLED));
            return;
        }
        if (!BluetoothAdapter.checkBluetoothAddress(deviceAddress)) {
            onError(new BluetoothException(BluetoothState.ADDRESS_NOT_VALID, deviceAddress));
            return;
        }

        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_ID));
            bluetoothAdapter.cancelDiscovery();
        } catch (IllegalArgumentException | IOException e) {
            onError(new BluetoothException(BluetoothState.UNKNOWN_ERROR, e));
            return;
        }

        try {
            socket.connect();
        } catch (IOException socketException) {
            closeSocket();
            if ("read failed, socket might closed or timeout, read ret: -1".equals(socketException.getMessage())) {
                onError(new BluetoothException(BluetoothState.SOCKET_CONNECT_ERROR));
            } else {
                onError(new BluetoothException(BluetoothState.SOCKET_CONNECT_ERROR, socketException));
            }
            return;
        }

        try {
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            disposable = Command.getCommandQueue().subscribe(this::sendCommand);
            Command.setTime();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                try {
                    String msg = reader.readLine();
                    if (handler != null) {
                        handler.obtainMessage(1, msg).sendToTarget();
                    }
                } catch (IOException readException) {
                    onError(new BluetoothException(BluetoothState.READ_ERROR, readException));
                    break;
                }
            }
        } catch (IOException socketException) {
            closeSocket();
            onError(new BluetoothException(BluetoothState.SOCKET_ERROR, socketException));
        }
    }

    private void onError(@NonNull BluetoothException throwable) {
        if (handler != null) {
            handler.obtainMessage(0, throwable).sendToTarget();
        } else {
            Timber.e("Could not close the connect socket: %s.", throwable.getMessage());
        }
    }

    public void cancel() {
        handler = null;
        closeSocket();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Timber.e("Can't close socket: %s", e.getMessage());
            }
            socket = null;
        }
    }

    private void sendCommand(String command) {
        if (outputStream != null) {
            try {
                outputStream.write(command);
                outputStream.newLine();
                outputStream.flush();
            } catch (IOException writeException) {
                onError(new BluetoothException(BluetoothState.WRITE_ERROR, writeException));
            }
        }
    }
}
