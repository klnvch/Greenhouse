package com.klnvch.greenhouseviewer.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.klnvch.greenhousecommon.firestore.ModuleStateHelper
import com.klnvch.greenhousecommon.firestore.PhoneStateHelper
import com.klnvch.greenhousecommon.firestore.StateHelper
import com.klnvch.greenhousecommon.models.ModuleState
import com.klnvch.greenhousecommon.models.PhoneState
import io.reactivex.Single

object StateReader {
    private const val LIMIT = 1000L
    private val phoneStateHelper = PhoneStateHelper()
    private val moduleStateHelper = ModuleStateHelper()

    @JvmStatic
    fun readPhoneStates(deviceId: String, startTime: Long): Single<List<PhoneState>> {
        return read(deviceId, startTime, phoneStateHelper, PhoneState::class.java)
    }

    @JvmStatic
    fun readModuleStates(deviceId: String, startTime: Long): Single<List<ModuleState>> {
        return read(deviceId, startTime, moduleStateHelper, ModuleState::class.java)
    }

    private fun <T> read(
        deviceId: String,
        startTime: Long,
        helper: StateHelper,
        classOfT: Class<T>
    ): Single<List<T>> {
        return Single.create { emitter ->
            FirebaseFirestore.getInstance()
                .collection(helper.collectionPath)
                .orderBy(StateHelper.FIELD_TIME)
                .whereEqualTo(StateHelper.FIELD_DEVICE_ID, deviceId)
                .whereGreaterThan(StateHelper.FIELD_TIME, startTime)
                .limit(LIMIT)
                .get()
                .addOnSuccessListener { documents ->
                    val result = documents.map { helper.fromMap(it.data, classOfT) }
                    emitter.onSuccess(result)
                }
                .addOnFailureListener { e -> emitter.tryOnError(e) }
        }
    }
}
