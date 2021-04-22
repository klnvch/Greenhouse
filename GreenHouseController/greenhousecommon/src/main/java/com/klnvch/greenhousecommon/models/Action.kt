package com.klnvch.greenhousecommon.models

import androidx.room.Entity

@Entity(primaryKeys = ["deviceId", "time"], tableName = "actions")
data class Action(
    val deviceId: String,
    val time: Long,
    val command: String,
    val state: Int
) {
    companion object {
        const val SENT = 1
        const val SUCCESS = 2
        const val FAIL = 3
    }
}
