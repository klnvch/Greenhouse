package com.klnvch.greenhousecontroller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.klnvch.greenhousecontroller.models.FireStoreData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class FireStoreUtils {
    private static final String TAG = "FireStoreUtils";

    static void saveFirebaseToken(String deviceId, String token) {
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        FirebaseFirestore.getInstance()
                .collection("settings")
                .document(deviceId)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Token successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing token", e));
    }

    static void saveToFireStore(String deviceId, @NonNull FireStoreData object) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss", Locale.getDefault());
        String collectionPath = object.getCollectionPath();
        String documentPath = sdf.format(new Date());

        Map<String, Object> data = object.toRow();
        data.put("deviceId", deviceId);
        PhoneStatusManager.addData(data);

        FirebaseFirestore.getInstance()
                .collection(collectionPath)
                .document(documentPath)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Object successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing object", e));
    }
}
