package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public abstract class FireStoreData {
    protected String id;

    @NonNull
    public abstract String getCollectionPath();

    @NonNull
    public abstract Map<String, Object> toRow();

    @NonNull
    public String generateDocumentPath() {
        if (id == null) {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd HH/mm/ss", Locale.getDefault());
            id = sdf.format(new Date());
        }
        return id;
    }

    public String getId() {
        return id;
    }
}
