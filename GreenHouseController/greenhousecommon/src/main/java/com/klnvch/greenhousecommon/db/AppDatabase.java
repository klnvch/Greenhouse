package com.klnvch.greenhousecommon.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Database(entities = {PhoneState.class}, version = 6, exportSchema = false)
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


    public Completable insert(@NonNull List<PhoneState> phoneStates) {
        return phoneStateDao().insert(phoneStates);
    }

    public Single<Long> getLatestPhoneStateTime(String deviceId) {
        return phoneStateDao().getLatestPhoneState(deviceId)
                .map(states -> {
                    if (states.size() > 0) {
                        return states.get(0).getTime();
                    } else {
                        return 0L;
                    }
                });
    }
}
