package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Info extends FireStoreData {
    private final String msg;

    public Info(@NonNull String msg) {
        this.msg = msg.replace("Info: ", "");
    }

    @NonNull
    @Override
    public String getCollectionPath() {
        return "info";
    }

    @NonNull
    @Override
    public Map<String, Object> toRow() {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return msg;
    }
}
