package com.klnvch.greenhousecontroller.models;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PhoneStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(PhoneState phoneState);

    @Query("SELECT * FROM phoneState WHERE deviceId=:deviceId AND time > :timeLimit ORDER BY time DESC")
    Flowable<List<PhoneState>> getLatestPhoneStates(String deviceId, long timeLimit);
}
