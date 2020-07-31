package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;

import java.util.Map;

public interface FireStoreData {
    @NonNull
    String getCollectionPath();

    @NonNull
    Map<String, Object> toRow();
}
