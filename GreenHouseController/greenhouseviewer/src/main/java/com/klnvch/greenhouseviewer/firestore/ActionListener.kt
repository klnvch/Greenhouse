package com.klnvch.greenhouseviewer.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber

class ActionListener {
    var registration: ListenerRegistration? = null

    fun startListening(listener: OnActionUpdatedListener) {
        stopListening()

        registration = FirebaseFirestore.getInstance()
            .collection("settings")
            .document("action")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed: %s.", e.message)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    processData(snapshot.data)
                } else {
                    Timber.d("Current data: null")
                }
            }
    }

    private fun processData(data : Map<String, Any>?) {

    }

    fun stopListening() {
        registration?.remove()
    }
}