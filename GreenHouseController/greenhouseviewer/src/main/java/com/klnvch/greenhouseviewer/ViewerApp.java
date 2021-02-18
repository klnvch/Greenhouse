package com.klnvch.greenhouseviewer;

import com.google.firebase.FirebaseApp;
import com.klnvch.greenhousecommon.App;
import com.klnvch.greenhouseviewer.di.DaggerViewerAppComponent;

public class ViewerApp extends App {
    @Override
    public void onCreate() {
        super.onCreate();

        DaggerViewerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        FirebaseApp.initializeApp(this);
    }
}
