package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(primaryKeys = {"id"}, tableName = "info")
public class Info extends FireStoreData {
    public static final String COLLECTION_PATH = "info";
    private String msg;

    public Info() {
        super();
    }

    @NonNull
    public static Info createFromMessage(@NonNull String msg) {
        Info info = new Info();
        info.setMsg(msg);
        return info;
    }

    @NonNull
    public static Info createFromData(@NonNull DocumentSnapshot documentSnapshot) {
        Info info = new Info();
        info.id = documentSnapshot.getId();
        info.msg = documentSnapshot.getData().get("msg").toString();
        return info;
    }

    @Ignore
    public Info(@NonNull String msg) {
        super();
        this.msg = msg.replace("Info: ", "");
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @NonNull
    @Override
    public String getCollectionPath() {
        return COLLECTION_PATH;
    }

    @NonNull
    @Override
    public Map<String, Object> toRow() {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg);
        return result;
    }

    public String[] getCsvRow() {
        String[] row = new String[1];
        row[0] = id;
        row[1] = msg;
        return row;
    }

    @NonNull
    @Override
    public String toString() {
        return msg;
    }
}
