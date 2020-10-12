package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.HashMap;
import java.util.Map;

@Entity(primaryKeys = {"id"}, tableName = "info")
public class Info extends FireStoreData {
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
