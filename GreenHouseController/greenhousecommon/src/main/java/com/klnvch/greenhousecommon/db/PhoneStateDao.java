package com.klnvch.greenhousecommon.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.klnvch.greenhousecommon.models.PhoneState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PhoneStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(PhoneState phoneState);

    @Query("SELECT * FROM phoneState WHERE deviceId=:arg0 ORDER BY time DESC LIMIT 100")
    Flowable<List<PhoneState>> getLatestPhoneStates(String arg0);
}
