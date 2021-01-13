package com.klnvch.greenhouseviewer.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.klnvch.greenhousecommon.firestore.PhoneStateHelperAbstract;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

public class PhoneStateHelper extends PhoneStateHelperAbstract {
    private static final int LIMIT = 1000;

    @Nullable
    private static PhoneState fromSnapshot(@NonNull DocumentSnapshot documentSnapshot) {
        String deviceId = documentSnapshot.getString(FIELD_DEVICE_ID);
        Long time = documentSnapshot.getLong(FIELD_TIME);
        Boolean isCharging = documentSnapshot.getBoolean("isCharging");
        Long batteryLevel = documentSnapshot.getLong("batteryLevel");
        Long networkStrength = documentSnapshot.getLong("networkStrength");
        Long bluetoothState = documentSnapshot.getLong("bluetoothState");
        String bluetoothError = documentSnapshot.getString("bluetoothError");

        if (deviceId != null && time != null && isCharging != null && batteryLevel != null && networkStrength != null && bluetoothState != null) {
            PhoneState result = new PhoneState();
            result.setDeviceId(deviceId);
            result.setTime(time);
            result.setCharging(isCharging);
            result.setBatteryLevel(batteryLevel.intValue());
            result.setNetworkStrength(networkStrength.intValue());
            result.setBluetoothState(bluetoothState.intValue());
            result.setBluetoothError(bluetoothError);
            return result;
        }
        return null;
    }

    public static Single<List<PhoneState>> read(long startTime) {
        return Single.create(emitter -> FirebaseFirestore.getInstance()
                .collection(COLLECTION_PATH)
                .orderBy(FIELD_TIME)
                .whereEqualTo(FIELD_DEVICE_ID, "test")
                .whereGreaterThan(FIELD_TIME, startTime)
                .limit(LIMIT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<PhoneState> phoneStates = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                PhoneState phoneState = fromSnapshot(documentSnapshot);
                                if (phoneState != null) {
                                    phoneStates.add(phoneState);
                                }
                            }
                        }
                        emitter.onSuccess(phoneStates);
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            Timber.e("Read for PhoneState failed: %s", exception.getMessage());
                        } else {
                            Timber.e("Read for PhoneState failed");
                        }
                        emitter.tryOnError(new Exception(exception));
                    }
                }));
    }
}
