package com.klnvch.greenhousecommon.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klnvch.greenhousecommon.models.Action
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(action: Action): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(actions: List<Action>): Completable

    @Query("SELECT * FROM actions WHERE deviceId=:arg0 ORDER BY time DESC LIMIT 1000")
    fun getActions(arg0: String): Flowable<List<Action>>
}
