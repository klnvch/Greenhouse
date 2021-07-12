package com.klnvch.greenhousecontroller.firestore;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.klnvch.greenhousecommon.firestore.ModuleStateHelper;
import com.klnvch.greenhousecommon.firestore.PhoneStateHelper;
import com.klnvch.greenhousecommon.firestore.StateHelper;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.Map;

import timber.log.Timber;

public class StateWriter {
    private static final PhoneStateHelper phoneStateHelper = new PhoneStateHelper();
    private static final ModuleStateHelper moduleStateHelper = new ModuleStateHelper();

    public synchronized static void save(@NonNull PhoneState state) {
        save(state, phoneStateHelper);
    }

    public synchronized static void save(@NonNull ModuleState state) {
        save(state, moduleStateHelper);
    }

    private synchronized static void save(@NonNull Object state, StateHelper helper) {
        Map<String, Object> data = helper.toMap(state);

        FirebaseFirestore.getInstance()
                .collection(helper.getCollectionPath())
                .document()
                .set(data)
                .addOnSuccessListener(aVoid -> Timber.d("%s successfully written!", state.getClass().getName()))
                .addOnFailureListener(e -> Timber.e("Error writing %s: %s", state.getClass().getName(), e.getMessage()));
    }
}
