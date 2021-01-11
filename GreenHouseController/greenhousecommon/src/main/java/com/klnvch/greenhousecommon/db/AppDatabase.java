package com.klnvch.greenhousecommon.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.klnvch.greenhousecommon.models.PhoneState;

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
}
