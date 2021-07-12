package com.klnvch.greenhousecontroller.logs;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class CustomTimberTree extends Timber.DebugTree {

    private CustomTimberTree() {
    }

    public static void plant() {
        Timber.uprootAll();
        Timber.plant();
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        super.log(priority, tag, message, t);
    }
}
