package com.klnvch.greenhousecontroller.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.klnvch.greenhousecontroller.models.AppDatabase;
import com.klnvch.greenhousecontroller.models.FireStoreData;
import com.klnvch.greenhousecontroller.models.Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static void saveToFireStore(String deviceId, @NonNull FireStoreData object) {
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

    @SuppressLint("LogNotTimber")
    public static void readInfoFromFireStore(Context context) {
        FirebaseFirestore.getInstance()
                .collection(Info.COLLECTION_PATH)
                .limit(10000)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                        List<Info> collection = new ArrayList<>();
                        for (DocumentSnapshot snapshot : documentSnapshots) {
                            Info info = Info.createFromData(snapshot);
                            collection.add(info);
                            deleteDocument(Info.COLLECTION_PATH, info.generateDocumentPath());
                        }
                        AppDatabase.getInstance(context).insertAll(collection);
                        Log.d("FireStore", "Fetched: " + collection.size());
                    } else {
                        Log.d("FireStore", "get failed with " + task.getException().toString());
                    }
                });
    }

    public static void deleteDocument(String collectionPath, String documentPath) {
        FirebaseFirestore.getInstance()
                .collection(collectionPath)
                .document(documentPath)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
