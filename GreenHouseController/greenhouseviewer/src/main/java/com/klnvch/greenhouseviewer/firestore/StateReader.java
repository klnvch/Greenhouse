package com.klnvch.greenhouseviewer.firestore;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.klnvch.greenhousecommon.firestore.ModuleStateHelper;
import com.klnvch.greenhousecommon.firestore.PhoneStateHelper;
import com.klnvch.greenhousecommon.firestore.StateHelper;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import timber.log.Timber;

public class StateReader {
    private static final int LIMIT = 1000;
    private static final String DEVICE_ID = "test";

    private static final PhoneStateHelper phoneStateHelper = new PhoneStateHelper();
    private static final ModuleStateHelper moduleStateHelper = new ModuleStateHelper();

    public static Single<List<PhoneState>> readPhoneStates(long startTime) {
        return read(startTime, phoneStateHelper, PhoneState.class);
    }

    public static Single<List<ModuleState>> readModuleStates(long startTime) {
        return read(startTime, moduleStateHelper, ModuleState.class);
    }

    public static <T> Single<List<T>> read(long startTime, StateHelper helper, Class<T> classOfT) {
        return Single.create(emitter -> FirebaseFirestore.getInstance()
                .collection(helper.getCollectionPath())
                .orderBy(StateHelper.FIELD_TIME)
                .whereEqualTo(StateHelper.FIELD_DEVICE_ID, DEVICE_ID)
                .whereGreaterThan(StateHelper.FIELD_TIME, startTime)
                .limit(LIMIT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<T> phoneStates = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                Map<String, Object> data = documentSnapshot.getData();
                                if (data != null) {
                                    T phoneState = helper.fromMap(data, classOfT);
                                    phoneStates.add(phoneState);
                                }
                            }
                        }
                        emitter.onSuccess(phoneStates);
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            Timber.e("Read for %s failed: %s", classOfT.getName(), exception.getMessage());
                        } else {
                            Timber.e("Read for %s failed", classOfT.getName());
                        }
                        emitter.tryOnError(new Exception(exception));
                    }
                }));
    }
}
