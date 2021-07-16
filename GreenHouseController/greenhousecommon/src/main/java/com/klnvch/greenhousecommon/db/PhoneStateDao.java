package com.klnvch.greenhousecommon.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PhoneStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(PhoneState state);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<PhoneState> states);

    @Query("SELECT * FROM phoneState WHERE deviceId=:arg0 AND time > :arg1 ORDER BY time DESC")
    Flowable<List<PhoneState>> getLatestStates(String arg0, long arg1);

    @Query("SELECT * FROM phoneState WHERE deviceId=:arg0 AND time > :arg1 ORDER BY time ASC")
    Single<List<PhoneState>> getStatesAscending(String arg0, long arg1);

    @Query("SELECT * FROM phoneState WHERE deviceId=:arg0 ORDER BY time DESC LIMIT 1")
    Single<List<PhoneState>> getLatestState(String arg0);
}
