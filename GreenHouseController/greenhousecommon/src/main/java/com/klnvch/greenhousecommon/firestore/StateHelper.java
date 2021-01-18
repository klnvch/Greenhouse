package com.klnvch.greenhousecommon.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.Map;

public abstract class StateHelper {
    public static final String FIELD_DEVICE_ID = "deviceId";
    public static final String FIELD_TIME = "time";
    private final ObjectMapper mapper;

    public StateHelper() {
        mapper = new ObjectMapper();
    }

    @NonNull
    public Map<String, Object> toMap(@NonNull Object object) {
        return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    @NonNull
    public  <T> T fromMap(@NonNull Map<String, Object> data, Class<T> classOfT) {
        return mapper.convertValue(data, classOfT);
    }

    public abstract String getCollectionPath();
}
