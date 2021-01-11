package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.BluetoothState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

public class BluetoothStateFragment extends ItemStateFragment implements PhoneStateInterface {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMessage(null);
    }

    @Override
    public void update(List<PhoneState> states) {
        if (states == null || states.size() == 0) {
            setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
            setDataMissingMessage();
            setAlert();
        } else {
            PhoneState latest = states.get(0);
            int bluetoothState = latest.getBluetoothState();
            String error = latest.getBluetoothError();
            switch (bluetoothState) {
                case BluetoothState.CONNECTED:
                    setImage(R.drawable.ic_baseline_bluetooth_connected_24);
                    setNormal();
                    break;
                case BluetoothState.NOT_AVAILABLE:
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
                    setMessage(R.string.state_message_bluetooth_not_available);
                    setAlert();
                    break;
                case BluetoothState.NOT_ENABLED:
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
                    setMessage(R.string.state_message_bluetooth_not_enabled);
                    setAlert();
                    break;
                case BluetoothState.ADDRESS_NOT_VALID:
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
                    setMessage(error);
                    setAlert();
                    break;
                case BluetoothState.READ_ERROR:
                case BluetoothState.WRITE_ERROR:
                case BluetoothState.SOCKET_ERROR:
                    setImage(R.drawable.ic_baseline_bluetooth_searching_24);
                    setMessage(error);
                    setAlert();
                    break;
                default:
                    setImage(R.drawable.ic_baseline_bluetooth_disabled_24);
                    setAlert();
            }
        }
    }
}
