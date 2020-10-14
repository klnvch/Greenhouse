package com.klnvch.greenhousecontroller.logs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.klnvch.greenhousecontroller.models.AppDatabase;
import com.klnvch.greenhousecontroller.models.Info;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class CustomTimberTree extends Timber.DebugTree {
    private final AppDatabase db;

    private CustomTimberTree(@NonNull AppDatabase db) {
        this.db = db;
    }

    public static void plant(@NonNull Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        Timber.uprootAll();
        Timber.plant(new CustomTimberTree(db));
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (priority > Log.DEBUG) {
            Info info = Info.createFromMessage(message);
            db.insert(info);
        }
        super.log(priority, tag, message, t);
    }
}
