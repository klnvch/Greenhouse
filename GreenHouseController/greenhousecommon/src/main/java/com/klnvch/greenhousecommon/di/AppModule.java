package com.klnvch.greenhousecommon.di;

import android.app.Application;

import androidx.room.Room;

import com.klnvch.greenhousecommon.db.ActionDao;
import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;
import com.klnvch.greenhousecommon.db.ModuleStateDao;
import com.klnvch.greenhousecommon.db.PhoneStateDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class AppModule {
    @Singleton
    @Provides
    public AppDatabase provideDb(Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "db_common")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public PhoneStateDao providePhoneStateDao(AppDatabase db) {
        return db.phoneStateDao();
    }

    @Singleton
    @Provides
    public ModuleStateDao provideModuleStateDao(AppDatabase db) {
        return db.moduleStateDao();
    }

    @Singleton
    @Provides
    public ActionDao provideActionDao(AppDatabase db) {
        return db.actionDao();
    }

    @Singleton
    @Provides
    public AppSettings provideSettings(Application application) {
        return new AppSettings(application);
    }
}
