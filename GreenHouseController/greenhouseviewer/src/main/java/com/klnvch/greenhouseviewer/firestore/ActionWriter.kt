package com.klnvch.greenhouseviewer.firestore

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.firestore.FirebaseFirestore
import com.klnvch.greenhousecommon.models.Action
import timber.log.Timber

class ActionWriter {
    companion object {
        fun write(action: Action) {
            val mapper = ObjectMapper()
            val data = mapper.convertValue(action, object : TypeReference<Map<String, Any>>() {})

            FirebaseFirestore.getInstance()
                .collection("settings")
                .document("action")
                .set(data)
                .addOnSuccessListener { Timber.d("Action written") }
                .addOnFailureListener { e -> Timber.e("Error action writing: %s", e.message) }
        }
    }
}