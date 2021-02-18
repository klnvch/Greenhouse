package com.klnvch.greenhousecommon;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.klnvch.greenhousecommon.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class App extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject
    protected DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    protected DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }
}
