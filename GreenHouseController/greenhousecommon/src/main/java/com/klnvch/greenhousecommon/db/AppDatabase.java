package com.klnvch.greenhousecommon.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.klnvch.greenhousecommon.models.Action;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Database(entities = {PhoneState.class, ModuleState.class, Action.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;

    @NonNull
    public synchronized static AppDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "db_common")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract PhoneStateDao phoneStateDao();

    public abstract ModuleStateDao moduleStateDao();

    public abstract ActionDao actionDao();

    public Single<Integer> insertPhoneStates(@NonNull List<PhoneState> states) {
        return phoneStateDao().insert(states)
                .toSingleDefault(states.size())
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> insertModuleStates(@NonNull List<ModuleState> states) {
        return moduleStateDao().insert(states)
                .toSingleDefault(states.size())
                .subscribeOn(Schedulers.io());
    }

    public Single<Long> getLatestPhoneStateTime(String deviceId) {
        return phoneStateDao().getLatestState(deviceId)
                .map(states -> {
                    if (states.size() > 0) {
                        return states.get(0).getTime();
                    } else {
                        return 0L;
                    }
                });
    }

    public Single<Long> getLatestModuleStateTime(String deviceId) {
        return moduleStateDao().getLatestState(deviceId)
                .map(states -> {
                    if (states.size() > 0) {
                        return states.get(0).getTime();
                    } else {
                        return 0L;
                    }
                });
    }
}
