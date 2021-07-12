package com.klnvch.greenhousecontroller.utils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class FireStoreUtils {

    public static void saveFirebaseToken(@NonNull String deviceId, @NonNull String token) {
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        FirebaseFirestore.getInstance()
                .collection("settings")
                .document(deviceId)
                .set(data)
                .addOnSuccessListener(aVoid -> Timber.d("Token successfully written to Firestore!"))
                .addOnFailureListener(e -> Timber.e("Error writing token: %s", e.getMessage()));
    }
}
