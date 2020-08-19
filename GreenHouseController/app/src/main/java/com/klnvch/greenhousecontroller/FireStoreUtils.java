package com.klnvch.greenhousecontroller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.klnvch.greenhousecontroller.models.FireStoreData;

import java.util.HashMap;
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
        String collectionPath = object.getCollectionPath();
        String documentPath = object.generateDocumentPath();

        Map<String, Object> data = object.toRow();
        data.put("deviceId", deviceId);

        FirebaseFirestore.getInstance()
                .collection(collectionPath)
                .document(documentPath)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Object successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing object", e));
    }
}
