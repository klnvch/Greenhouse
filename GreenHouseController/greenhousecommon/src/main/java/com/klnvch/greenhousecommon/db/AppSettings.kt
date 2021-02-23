package com.klnvch.greenhousecommon.db

import android.content.Context
import android.content.SharedPreferences

class AppSettings(context: Context) {
    var sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun getDeviceId(): String {
        return sharedPreferences.getString(KEY_DEVICE_ID, null) ?: DEFAULT_DEVICE_ID
    }

    fun setDeviceId(deviceId: String?) {
        sharedPreferences.edit().putString(KEY_DEVICE_ID, deviceId).apply();
    }

    companion object {
        private const val NAME = "settings"
        private const val KEY_DEVICE_ID = "deviceId"
        private const val DEFAULT_DEVICE_ID = "test"
    }
}