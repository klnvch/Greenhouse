package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public abstract class FireStoreData {
    private static final String ID_PATTERN = "yyyyMMddHHmmss";
    private static final String PATTERN_FOR_LOGS = "MM-dd HH:mm";

    @NonNull
    protected String id;

    FireStoreData() {
        SimpleDateFormat sdf = new SimpleDateFormat(ID_PATTERN, Locale.getDefault());
        id = sdf.format(new Date());
    }

    @NonNull
    public abstract String getCollectionPath();

    @NonNull
    public abstract Map<String, Object> toRow();

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String generateDocumentPath() {
        return id;
    }

    @Nullable
    public String getTimeForLogs() {
        SimpleDateFormat sdf = new SimpleDateFormat(ID_PATTERN, Locale.getDefault());
        try {
            Date date = sdf.parse(id);
            if (date != null) {
                return new SimpleDateFormat(PATTERN_FOR_LOGS, Locale.getDefault()).format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
