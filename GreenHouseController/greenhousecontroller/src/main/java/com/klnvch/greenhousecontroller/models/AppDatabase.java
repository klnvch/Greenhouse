package com.klnvch.greenhousecontroller.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

@Database(entities = {Data.class, Info.class, PhoneData.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;

    @NonNull
    public synchronized static AppDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract DataDao dataDao();

    /**
     * @return dao for info messages
     */
    public abstract InfoDao infoDao();

    public abstract PhoneDataDao phoneDataDao();

    public void insert(PhoneData phoneData) {
        phoneDataDao().insert(phoneData)
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe();
    }

    public void insert(Info info) {
        infoDao().insertAll(info)
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe();
    }

    public void insertAll(List<Info> info) {
        infoDao().insertAll(info)
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe();
    }
}
