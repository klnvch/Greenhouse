package com.klnvch.greenhousecontroller;

import com.klnvch.greenhousecommon.App;
import com.klnvch.greenhousecontroller.di.DaggerControllerAppComponent;

public class ControllerApp extends App {
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerControllerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }
}
