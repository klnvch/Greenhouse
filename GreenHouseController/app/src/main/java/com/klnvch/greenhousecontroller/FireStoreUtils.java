package com.klnvch.greenhousecontroller;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.klnvch.greenhousecontroller.models.FireStoreData;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

class FireStoreUtils {
    static void saveFirebaseToken(String deviceId, String token) {
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        FirebaseFirestore.getInstance()
                .collection("settings")
                .document(deviceId)
                .set(data)
                .addOnSuccessListener(aVoid -> Timber.d("Token successfully written!"))
                .addOnFailureListener(e -> Timber.e("Error writing token: %s", e.getMessage()));
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
                .addOnSuccessListener(aVoid -> Timber.d("Object successfully written!"))
                .addOnFailureListener(e -> Timber.e("Error writing object: %s", e.getMessage()));
    }
}
