package com.klnvch.greenhousecontroller.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.klnvch.greenhousecommon.firestore.ModuleStateHelper
import com.klnvch.greenhousecommon.firestore.PhoneStateHelper
import com.klnvch.greenhousecommon.firestore.StateHelper
import com.klnvch.greenhousecommon.models.ModuleState
import com.klnvch.greenhousecommon.models.PhoneState
import timber.log.Timber

class StateWriter(private val firebaseFirestore: FirebaseFirestore) {
    private val phoneStateHelper = PhoneStateHelper()
    private val moduleStateHelper = ModuleStateHelper()

    fun save(state: PhoneState) {
        save(state, phoneStateHelper)
    }

    fun save(state: ModuleState) {
        save(state, moduleStateHelper)
    }

    private fun save(state: Any, helper: StateHelper) {
        val data = helper.toMap(state)
        firebaseFirestore
            .collection(helper.collectionPath)
            .document()
            .set(data)
            .addOnSuccessListener {
                Timber.d(
                    "%s successfully written!",
                    state.javaClass.name
                )
            }
            .addOnFailureListener { e: Exception ->
                Timber.e(
                    "Error writing %s: %s",
                    state.javaClass.name,
                    e.message
                )
            }
    }
}
