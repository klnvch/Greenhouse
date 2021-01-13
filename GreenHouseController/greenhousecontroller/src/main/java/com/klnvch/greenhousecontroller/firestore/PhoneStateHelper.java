package com.klnvch.greenhousecontroller.firestore;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.klnvch.greenhousecommon.firestore.PhoneStateHelperAbstract;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class PhoneStateHelper extends PhoneStateHelperAbstract {
    private static Map<String, Object> toMap(@NonNull PhoneState phoneState) {
        Map<String, Object> result = new HashMap<>();
        result.put(FIELD_DEVICE_ID, phoneState.getDeviceId());
        result.put(FIELD_TIME, phoneState.getTime());
        result.put("isCharging", phoneState.isCharging());
        result.put("batteryLevel", phoneState.getBatteryLevel());
        result.put("networkStrength", phoneState.getNetworkStrength());
        result.put("bluetoothState", phoneState.getBluetoothState());
        result.put("bluetoothError", phoneState.getBluetoothError());
        return result;
    }

    public synchronized static void save(@NonNull PhoneState phoneState) {
        Map<String, Object> data = toMap(phoneState);

        FirebaseFirestore.getInstance()
                .collection(COLLECTION_PATH)
                .document()
                .set(data)
                .addOnSuccessListener(aVoid -> Timber.d("PhoneState successfully written!"))
                .addOnFailureListener(e -> Timber.e("Error writing phoneState: %s", e.getMessage()));
    }
}
