package com.klnvch.greenhousecommon.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.klnvch.greenhousecommon.models.ModuleState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ModuleStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ModuleState state);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<ModuleState> states);

    @Query("SELECT * FROM moduleState WHERE deviceId=:arg0 ORDER BY time DESC LIMIT 1000")
    Flowable<List<ModuleState>> getLatestStates(String arg0);

    @Query("SELECT * FROM moduleState WHERE deviceId=:arg0 ORDER BY time DESC LIMIT 1")
    Single<List<ModuleState>> getLatestState(String arg0);
}
