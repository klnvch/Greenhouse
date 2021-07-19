package com.klnvch.greenhousecommon.firestore

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

abstract class StateHelper {
    private val mapper: ObjectMapper = ObjectMapper()

    fun toMap(data: Any): Map<String, Any> {
        return mapper.convertValue(data, object : TypeReference<Map<String, Any>>() {})
    }

    fun <T> fromMap(data: Map<String, Any>, classOfT: Class<T>): T {
        return mapper.convertValue(data, classOfT)
    }

    abstract val collectionPath: String

    companion object {
        const val FIELD_DEVICE_ID = "deviceId"
        const val FIELD_TIME = "time"
    }

    init {
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
